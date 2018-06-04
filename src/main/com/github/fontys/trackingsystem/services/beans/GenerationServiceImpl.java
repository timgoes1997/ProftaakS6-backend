package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.tracking.DistanceCalculator;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.domain.EULocation;
import com.nonexistentcompany.domain.Route;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class GenerationServiceImpl implements GenerationService {

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private LocationService locationService;

    @Inject
    private BillService billService;

    @Inject
    private BillDAO billDAO;

    private RouteEngine routeEngine = new RouteEngine("DE");

    private DistanceCalculator distanceCalculator = new DistanceCalculator();

    // Returns void since this method will be called by an automated process
    // Called when a Rekeningrijder has stopped driving
    public void generateBillForLastMonthsRoutes(long registeredVehicleId) throws IOException, TimeoutException {
        String current = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);

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
        List<EULocation> euLocations = convertLocationsToEULocations(
                registeredVehicle.getLicensePlate(),
                lastMonthsLocations);

        // Foreign route //
        // Determine foreign routes and send them to the corresponding country
        Map<String, Route> routeMap = routeEngine.determineForeignRoutes(euLocations);
        routeEngine.sendRoutesToTheirCountry(routeMap);

        // Domestic route //
        // Get the EULocations within the hosts country
        List<EULocation> euLocationsDomestic = routeEngine.determineHomeRoute(euLocations);
        euLocationsDomestic.sort(EULocation::compareTo);

        // Calculate distance of domestic route
        double distanceInKilometers = calculateDistance(euLocationsDomestic);

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
        List<EULocation> euLocations = convertLocationsToEULocations(registeredVehicle.getLicensePlate(),
                route);

        // Foreign route //
        Map<String, Route> routeMap = routeEngine.determineForeignRoutes(euLocations);
        try {
            routeEngine.sendRoutesToTheirCountry(routeMap);
        } catch (Exception e){}

        // Domestic route //
        List<EULocation> euLocationsDomestic = routeEngine.determineHomeRoute(euLocations);
        euLocationsDomestic.sort(EULocation::compareTo);

        // Calculate the distance
        double distanceInKilometers = calculateDistance(euLocationsDomestic);

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
        System.out.println("Current dir:" + current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);

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
                    locations.get(i).getLon(),
                    locations.get(i + 1).getLat(),
                    locations.get(i + 1).getLon());
        }

        return distanceInKilometers;
    }

    public List<EULocation> convertLocationsToEULocations(String license, List<Location> locations) {
        List<EULocation> euLocations = new ArrayList<>();
        if (locations != null) {
            for (Location l : locations) {
                long unixTime = l.getTime().getTimeInMillis() / 1000;
                euLocations.add(new EULocation(license, l.getX(), l.getY(), unixTime));
            }
        }
        // Sort the EULocations list
        euLocations.sort(EULocation::compareTo);
        return euLocations;
    }

    public static Calendar convertDateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
