package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
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
    private TrackedVehicleDAO trackedVehicleDAO;

    @Inject
    private LocationService locationService;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private BillDAO billDAO;

    RouteEngine routeEngine = new RouteEngine("DE");

    DistanceCalculator distanceCalculator = new DistanceCalculator();

    // Returns void since this method will be called by an automated process
    // Called when a Rekeningrijden has stopped driving
    public void generateBillForLastMonthsRoutes(long registeredVehicleId) throws IOException, TimeoutException {

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
        List<EULocation> euLocations = convertLocationsToEULocations(lastMonthsLocations);

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
        // TODO: Recover rates per zone
        double price = generatePriceWithSingleRate(distanceInKilometers, 0.20);


        // Create new Bill //
        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(0),
                startDate,
                endDate,
                PaymentStatus.OPEN,
                distanceInKilometers);

        // Persist Bill
        billDAO.create(domesticRouteBill);
    }

    // StartDate should be stored somewhere at the point when driving is started
    // EndDate should be the point this method is called (after driving)
        public void generateBillsForLastRoute(String startDateString, String endDateString, long registeredVehicleId) throws IOException, TimeoutException {
        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByVehicle(registeredVehicleId);
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
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
        List<Location> route = locationService.getLocationsBetweenDatesByVehicleLicense(registeredVehicle.getLicensePlate(),
                startDateString,
                endDateString);

        // Convert last route from Location objects to EULocations objects
        List<EULocation> euLocations = convertLocationsToEULocations(route);

        // Foreign route //
        Map<String, Route> routeMap = routeEngine.determineForeignRoutes(euLocations);
        routeEngine.sendRoutesToTheirCountry(routeMap);

        // Domestic route //
        List<EULocation> euLocationsDomestic = routeEngine.determineHomeRoute(euLocations);
        euLocationsDomestic.sort(EULocation::compareTo);

        // Calculate the distance
        double distanceInKilometers = calculateDistance(euLocationsDomestic);

        // Calculate the price
        double price = generatePriceWithSingleRate(distanceInKilometers, 0.20);

        // Create new Bill //
        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(0),
                toCalendar(startDate),
                toCalendar(endDate),
                PaymentStatus.OPEN,
                distanceInKilometers);

        billDAO.create(domesticRouteBill);
        }

    private double generatePriceWithSingleRate(double distance, double rate) {
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

    private double calculateDistance(List<EULocation> locations) {
        double distanceInKilometers = 0;

        // Calculate distance
        for (int i = 0; i < (locations.size() - 1); i++) {
            distanceInKilometers = distanceCalculator.getDistance(locations.get(i).getLat(),
                    locations.get(i).getLon(),
                    locations.get(i + 1).getLat(),
                    locations.get(i + 1).getLon());
        }

        return distanceInKilometers;
    }

    private List<EULocation> convertLocationsToEULocations(List<Location> locations) {
        List<EULocation> euLocations = new ArrayList<>();
        if (locations != null) {
            for (Location l : locations) {
                long unixTime = l.getTime().getTimeInMillis() / 1000;
                euLocations.add(new EULocation(l.getX(), l.getY(), unixTime));
            }
        }
        // Sort the EULocations list
        euLocations.sort(EULocation::compareTo);
        return euLocations;
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
