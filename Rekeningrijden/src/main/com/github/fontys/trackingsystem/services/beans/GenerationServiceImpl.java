package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.interfaces.*;
import com.github.fontys.entities.tracking.DistanceCalculator;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.domain.EULocation;
import com.nonexistentcompany.lib.domain.ForeignRoute;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class GenerationServiceImpl implements GenerationService {

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private LocationService locationService;

    @Inject
    private BillService billService;

    @Inject
    private BillDAO billDAO;

    @Inject
    private RegionService regionService;

    @Inject
    private RouteService routeService;

    @Inject
    private Logger logger;

    private RouteEngine routeEngine = new RouteEngine("DE");

    private DistanceCalculator distanceCalculator = new DistanceCalculator();

    @Override
    public void generateBill(long registeredVehicleId, Calendar startDate, Calendar endDate) {
        if (!registeredVehicleDAO.exists(registeredVehicleId)) {
            throw new NotFoundException("Couldn't find registered vehicle");
        }

        RegisteredVehicle registeredVehicle = registeredVehicleDAO.find(registeredVehicleId);
        List<Location> locations = locationService.getLocationsBetweenTimesByVehicleLicense(
                registeredVehicle.getLicensePlate(),
                startDate.getTime(),
                endDate.getTime());

        List<Route> routes = routeService.generateRoutes(locations, registeredVehicle.getVehicle().getEnergyLabel());

        //EU gebeuren moet hier nog.
        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                routeService.getTotalPriceRoutes(routes),
                BigDecimal.valueOf(0),
                startDate,
                endDate,
                PaymentStatus.OPEN,
                routeService.getTotalDistanceRoutes(routes),
                true,
                routes);

        // add bill
        registeredVehicle.getBills().add(domesticRouteBill);

        // persist bill
        registeredVehicleDAO.edit(registeredVehicle);
    }

    @Override
    public void regenerateBill(long billId) {
        if (!billDAO.exists(billId)) {
            throw new NotFoundException("Given bill was not found!");
        }

        Bill bill = billDAO.find(billId);
        List<Location> locations = locationService.getLocationsBetweenTimesByVehicleLicense(
                bill.getRegisteredVehicle().getLicensePlate(),
                bill.getCalendarStartDate().getTime(),
                bill.getCalendarEndDate().getTime());

        List<Route> routes = routeService.generateRoutes(
                locations,
                bill.getRegisteredVehicle().
                        getVehicle().
                        getEnergyLabel());

        //Foreign country calculation shizzle

        bill.setPrice(routeService.getTotalPriceRoutes(routes));
        bill.setMileage(routeService.getTotalDistanceRoutes(routes));
        bill.setBillRoutes(routes);
        billDAO.edit(bill);
    }

    // Returns void since this method will be called by an automated process
    // Called when a Rekeningrijder has stopped driving
    public void generateBillForLastMonthsRoutes(long registeredVehicleId) throws IOException, TimeoutException {
        String current = new java.io.File(".").getCanonicalPath();
        logger.info("Current dir:" + current);
        String currentDir = System.getProperty("user.dir");
        logger.info("Current dir using System:" + currentDir);

        Calendar startDate = getFirstOfLastMonth();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = format1.format(startDate.getTime());
        Calendar endDate = getLastOfLastMonth();
        String endDateString = format1.format(endDate.getTime());

        // Get the RegisteredVehicle
        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByVehicle(registeredVehicleId);

        // Get locations of last month for this RegisteredVehicle
        List<Location> lastMonthsLocations = locationService.getLocationsBetweenDatesByVehicleLicense(
                registeredVehicle.getLicensePlate(),
                startDateString,
                endDateString);

        // Convert routes to EULocations
        List<EULocation> euLocations = routeService.convertLocationsToEULocations(
                registeredVehicle.getLicensePlate(),
                lastMonthsLocations);

        // Foreign route //
        // Determine foreign routes and send them to the corresponding country
        Map<String, ForeignRoute> routeMap = routeEngine.determineForeignRoutes(euLocations, registeredVehicle.getLicensePlate());
        routeEngine.sendRoutesToTheirCountry(routeMap);

        // Domestic route //
        // Get the EULocations within the hosts country
        ForeignRoute euForeignRoute = routeEngine.determineHomeRoute(euLocations, registeredVehicle.getLicensePlate());
        List<List<EULocation>> euLocationsDomestic = euForeignRoute.getTrips();
        for (List<EULocation> locations : euLocationsDomestic) {
            locations.sort(EULocation::compareTo);
        }

        // Calculate distance of domestic route
        double distanceInKilometers = 0.0d;
        for (List<EULocation> locations : euLocationsDomestic) {
            distanceInKilometers += calculateDistance(locations);
        }

        // Calculate price for domestic route
        // TODO: Recover rates per zone (CREATE METHOD List<Route> or smth generatePricePerRegion)
        double price = generatePriceWithSingleRate(distanceInKilometers, 0.20);


        // Create new Bill //
        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(0),
                startDate,
                endDate,
                PaymentStatus.OPEN,
                distanceInKilometers,
                true);

        // add bill
        registeredVehicle.getBills().add(domesticRouteBill);

        // persist bill
        registeredVehicleDAO.edit(registeredVehicle);
    }

    // StartDate should be stored somewhere at the point when driving is started
    // EndDate should be the point this method is called (after driving)
    // TODO: VehicleID to Licenseplate

    /**
     * Generates a bill, sends information to their respective country
     */
    public void handleLastRoute(String startDateString, String endDateString, String license) throws IOException, TimeoutException {
        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByLicense(license);
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;

        // Parse dates
        try {
            startDate = parse.parse(startDateString);
            endDate = parse.parse(endDateString);
        } catch (ParseException e) {
            throw new BadRequestException();
        }

        // Get locations of last route
        List<Location> route = locationService.getLocationsBetweenTimesByVehicleLicense(registeredVehicle.getLicensePlate(),
                startDateString,
                endDateString);

        // Convert last route from Location objects to EULocations objects
        List<EULocation> euLocations = routeService.convertLocationsToEULocations(registeredVehicle.getLicensePlate(),
                route);

        // Foreign route //
        Map<String, ForeignRoute> routeMap = routeEngine.determineForeignRoutes(euLocations, registeredVehicle.getLicensePlate());
        try {
            routeEngine.sendRoutesToTheirCountry(routeMap);
        } catch (Exception e) {
        }

        // Domestic route //
        // Get the EULocations within the hosts country
        ForeignRoute euForeignRoute = routeEngine.determineHomeRoute(euLocations, registeredVehicle.getLicensePlate());
        List<List<EULocation>> euLocationsDomestic = euForeignRoute.getTrips();
        for (List<EULocation> locations : euLocationsDomestic) {
            locations.sort(EULocation::compareTo);
        }

        // Calculate distance of domestic route
        double distanceInKilometers = 0.0d;
        for (List<EULocation> locations : euLocationsDomestic) {
            distanceInKilometers += calculateDistance(locations);
        }

        // Calculate the price
        double price = generatePriceWithSingleRate(distanceInKilometers, 0.20);

        // TODO: Calculate the price per region (CREATE METHOD List<Route> or smth generatePricePerRegion)

        // Create new Bill //
        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(0),
                convertDateToCalendar(startDate),
                convertDateToCalendar(endDate),
                PaymentStatus.OPEN,
                distanceInKilometers,
                false);


        billDAO.create(domesticRouteBill);
    }

    public void generateBillByLastMonthsRouteBills(long registeredVehicleId) throws IOException {
        String current = new java.io.File(".").getCanonicalPath();
        logger.info("Current dir:" + current);
        String currentDir = System.getProperty("user.dir");
        logger.info("Current dir using System:" + currentDir);

        Calendar startDate = getFirstOfLastMonth();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = format1.format(startDate.getTime());
        Calendar endDate = getLastOfLastMonth();
        String endDateString = format1.format(endDate.getTime());

        // Get the RegisteredVehicle
        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByVehicle(registeredVehicleId);

        // Collect all Bills for this vehicle of last month, excluding a possible totalBill
        List<Bill> lastMonthsBills = billService.getBillsBetweenDatesByVehicleId(registeredVehicleId, startDateString, endDateString, true);

        // Create the totalbill and iterator to loop through the routebills of last month
        Iterator<Bill> locIter = lastMonthsBills.iterator();
        Bill totalBill = new Bill();
        totalBill.setStartDate(startDate);
        totalBill.setEndDate(endDate);
        totalBill.setEndOfMonthBill(true);
        totalBill.setStatus(PaymentStatus.OPEN);
        totalBill.setAlreadyPaid(BigDecimal.ZERO);
        totalBill.setMontlyBills(lastMonthsBills);

        // Add the mileage and price of routebills to the totals
        while (locIter.hasNext()) {
            Bill b = locIter.next();
            totalBill.setMileage(totalBill.getMileage() + b.getMileage());
            totalBill.setPrice(totalBill.getPrice().add(b.getPrice()));
        }

        // Set the associated vehicle
        totalBill.setRegisteredVehicle(registeredVehicle);

        // Persist into database
        billDAO.create(totalBill);
    }


    public double generatePriceWithSingleRate(double distance, double rate) {
        return (distance * rate);
    }

    private Calendar getFirstOfLastMonth() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        return startDate;
    }

    private Calendar getLastOfLastMonth() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, -1);
        endDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        return endDate;
    }

    public double calculateDistance(List<EULocation> locations) {
        double distanceInKilometers = 0;

        // Calculate distance
        for (int i = 0; i < locations.size() - 1; i++) {
            distanceInKilometers = distanceCalculator.getDistance(locations.get(i).getLat(),
                    locations.get(i).getLng(),
                    locations.get(i + 1).getLat(),
                    locations.get(i + 1).getLng());
        }

        return distanceInKilometers;
    }

    @Override
    public double getDistance(List<Location> locations) {
        locations.sort(Comparator.comparing(Location::getTime));
        double distance = 0.0d;
        for (int i = locations.size() - 2, j = locations.size() - 1; i >= 0; i--, j--) {
            distance += distanceCalculator.getDistance(
                    locations.get(i).getLat(),
                    locations.get(i).getLon(),
                    locations.get(j).getLat(),
                    locations.get(j).getLon());
        }
        return distance;
    }

    /**
     * Calculates the price based upon the given locations. Might need to change to trips etc.
     *
     * @param locations
     * @return
     */
    public BigDecimal calculatePrice(List<Location> locations, EnergyLabel energyLabel) {
        BigDecimal price = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
        for (int i = locations.size() - 2, j = locations.size() - 1; i > 0; i--, j--) {
            Rate rate = regionService.getRate(locations.get(i), energyLabel);
            double distanceInKilometers = distanceCalculator.getDistance(
                    locations.get(i).getLat(),
                    locations.get(i).getLon(),
                    locations.get(j).getLat(),
                    locations.get(j).getLon());
            BigDecimal locationPrice = rate.getKilometerPrice().multiply(new BigDecimal(distanceInKilometers));
            price = price.add(locationPrice);
        }
        return price;
    }

    public static Calendar convertDateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void setRegisteredVehicleDAO(RegisteredVehicleDAO registeredVehicleDAO) {
        this.registeredVehicleDAO = registeredVehicleDAO;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public void setBillService(BillService billService) {
        this.billService = billService;
    }

    public void setBillDAO(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setRouteService(RouteService routeService) {
        this.routeService = routeService;
    }
}
