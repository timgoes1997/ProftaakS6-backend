package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.tracking.DistanceCalculator;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.domain.EULocation;
import com.nonexistentcompany.domain.Route;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class GenerationServiceImpl implements GenerationService {

    @Inject
    private TrackedVehicleDAO trackedVehicleDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private BillDAO billDAO;

    RouteEngine routeEngine = new RouteEngine("DE");

    DistanceCalculator distanceCalculator = new DistanceCalculator();

    // Returns void since this method will be called by an automated process
    // Called when a Rekeningrijden has stopped driving
    @Override
    public void generateBillForRoute(long registeredVehicleId) throws IOException, TimeoutException {

        // Get locations of last month for this registeredvehicle
        List<Location> lastMonthsLocations = trackedVehicleDAO.findLastMonthsLocationsByRegisteredVehicleID(registeredVehicleId);

        // Convert routes to EULocations
        List<EULocation> euLocations = new ArrayList<>();
        for (Location l : lastMonthsLocations) {
            long unixTime = l.getTime().getTimeInMillis() / 1000;
            euLocations.add(new EULocation(l.getX(), l.getY(), unixTime));
        }

        // Sort the EULocations list
        euLocations.sort(EULocation::compareTo);

        // Determine foreign routes and send them to the corresponding country
        Map<String, Route> routeMap = routeEngine.determineForeignRoutes(euLocations);
        routeEngine.sendRoutesToTheirCountry(routeMap);

        // Domestic route //
        double distanceInKilometers = 0;

        // Get the EULocations within the hosts country
        List<EULocation> euLocationsDomestic = routeEngine.determineHomeRoute(euLocations);
        euLocationsDomestic.sort(EULocation::compareTo);

        // Calculate distance
        for (int i = 0; i < (euLocations.size() - 1); i++) {
            distanceInKilometers = distanceCalculator.getDistance(euLocations.get(i).getLat(),
                    euLocations.get(i).getLon(),
                    euLocations.get(i + 1).getLat(),
                    euLocations.get(i + 1).getLon());
        }

        // Calculate price for domestic route
        // TODO: Recover rates per zone
        double price = generatePriceWithSingleRate(distanceInKilometers, 0.20);


        // Create new Bill //
        // Get registeredvehicle
        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByVehicle(registeredVehicleId);

        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                BigDecimal.valueOf(price),
                BigDecimal.valueOf(0),
                getFirstOfLastMonth(),
                getLastOfLastMonth(),
                PaymentStatus.OPEN,
                distanceInKilometers);

        // Persist Bill
        billDAO.create(domesticRouteBill);
    }

    @Override
    public List<Bill> generateBillsForVehicle(long vehicleId) {
        return null;
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
}
