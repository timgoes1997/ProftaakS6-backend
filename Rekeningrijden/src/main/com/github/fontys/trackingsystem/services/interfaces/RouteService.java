package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.payment.RouteDetail;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;

import java.math.BigDecimal;
import java.util.List;

public interface RouteService {
    List<Route> generateRoutes(List<Location> locations, EnergyLabel energyLabel);
    Route generateSingleRoute(List<Location> locations, EnergyLabel energyLabel);
    List<RouteDetail> generateRouteDetails(List<Location> locations, EnergyLabel energyLabel);
    RouteDetail generateSingleRouteDetails(List<Location> locations, Rate rate);

    BigDecimal getTotalPriceRouteDetails(List<RouteDetail> routeDetails);
    double getTotalDistanceRouteDetails(List<RouteDetail> routeDetails);

    long getTimeDifferenceMinutes(Location i, Location j);
    boolean differenceIsGreaterOrEquals(long differenceMinutes, Location i, Location j);
}
