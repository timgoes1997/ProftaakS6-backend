package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.payment.RouteDetail;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.nonexistentcompany.lib.domain.EULocation;

import java.math.BigDecimal;
import java.util.List;

public interface RouteService {
    List<Route> generateRoutes(List<Location> locations, EnergyLabel energyLabel);
    Route generateSingleRoute(List<Location> locations, EnergyLabel energyLabel);
    List<RouteDetail> generateRouteDetails(List<Location> locations, EnergyLabel energyLabel);
    RouteDetail generateSingleRouteDetails(List<Location> locations, Rate rate);

    BigDecimal getTotalPriceRouteDetails(List<RouteDetail> routeDetails);
    double getTotalDistanceRouteDetails(List<RouteDetail> routeDetails);

    double getDistance(List<Location> locations);

    BigDecimal getTotalPriceRoutes(List<Route> routes);
    double getTotalDistanceRoutes(List<Route> routes);

    long getTimeDifferenceMinutes(Location i, Location j);
    boolean differenceIsGreaterOrEquals(long differenceMinutes, Location i, Location j);

    List<EULocation> convertLocationsToEULocations(String license, List<Location> locations);

    List<Location> convertEULocationsToLocation(String id, List<List<EULocation>> euLocations);
}
