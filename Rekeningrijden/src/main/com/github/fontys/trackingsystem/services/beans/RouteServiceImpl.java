package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.payment.RouteDetail;
import com.github.fontys.entities.tracking.DistanceCalculator;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.RegionService;
import com.github.fontys.trackingsystem.services.interfaces.RouteService;
import com.nonexistentcompany.lib.domain.EULocation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class RouteServiceImpl implements RouteService {

    private static long GENERATION_ROUTE_STEP = 15;
    private static long MILLIS_TO_SECONDS = 1000;
    private static long SECONDS_TO_MINUTES = 60;

    @Inject
    private RegionService regionService;

    @Override
    public Route generateSingleRoute(List<Location> locations, EnergyLabel energyLabel) {
        locations.sort(Comparator.comparing(Location::getTime));
        List<RouteDetail> routeDetails = generateRouteDetails(locations, energyLabel);
        return new Route(
                locations.get(0).getTime(),
                locations.get(locations.size() - 1).getTime(),
                locations,
                getTotalDistanceRouteDetails(routeDetails),
                getTotalPriceRouteDetails(routeDetails),
                routeDetails);
    }

    @Override
    public double getTotalDistanceRouteDetails(List<RouteDetail> routeDetails) {
        double distance = 0.0d;
        for (RouteDetail routeDetail : routeDetails) {
            distance += routeDetail.getDistance();
        }
        return distance;
    }

    @Override
    public BigDecimal getTotalPriceRouteDetails(List<RouteDetail> routeDetails) {
        BigDecimal price = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        for (RouteDetail routeDetail : routeDetails) {
            price = price.add(routeDetail.getPrice());
        }
        return price;
    }

    @Override
    public List<RouteDetail> generateRouteDetails(List<Location> locations, EnergyLabel energyLabel) {
        List<RouteDetail> routeDetails = new ArrayList<>();
        Set<Location> currentRouteDetailsLocations = new HashSet<>();
        for (int i = 0, j = 1; j < locations.size(); i++, j++) {
            Location locI = locations.get(i);
            Location locJ = locations.get(j);

            Rate rateLocI = regionService.getRate(locI, energyLabel);
            Rate rateLocJ = regionService.getRate(locJ, energyLabel);
            if (rateLocI.getId().equals(rateLocJ.getId())) {
                currentRouteDetailsLocations.add(locI);
            } else {
                currentRouteDetailsLocations.add(locI);
                currentRouteDetailsLocations.add(locJ);
                routeDetails.add(generateSingleRouteDetails(new ArrayList<>(currentRouteDetailsLocations), rateLocI));
                currentRouteDetailsLocations = new HashSet<>();
            }

            if(j == locations.size() - 1){
                currentRouteDetailsLocations.add(locJ);
                routeDetails.add(generateSingleRouteDetails(new ArrayList<>(currentRouteDetailsLocations), rateLocI));
            }
        }

        return routeDetails;
    }

    @Override
    public RouteDetail generateSingleRouteDetails(List<Location> locations, Rate rate) {
        double distance = getDistance(locations);
        BigDecimal price = rate.getKilometerPrice().multiply(new BigDecimal(distance)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return new RouteDetail(
                locations.get(0).getTime(),
                locations.get(locations.size() - 1).getTime(),
                distance,
                price,
                rate);
    }

    @Override
    public List<Route> generateRoutes(List<Location> locations, EnergyLabel energyLabel) {
        List<Route> routes = new ArrayList<>();
        List<Location> currentLocationsRoute = new ArrayList<>();

        for (int i = 0, j = 1; j < locations.size(); i++, j++) {
            Location locI = locations.get(i);
            Location locJ = locations.get(j);

            currentLocationsRoute.add(locI);

            if (differenceIsGreaterOrEquals(GENERATION_ROUTE_STEP, locI, locJ) && currentLocationsRoute.size() > 1) {
                routes.add(generateSingleRoute(currentLocationsRoute, energyLabel));
                currentLocationsRoute = new ArrayList<>();
            }

            if(j == locations.size() - 1){
                currentLocationsRoute.add(locJ);
                routes.add(generateSingleRoute(currentLocationsRoute, energyLabel));
            }
        }

        return routes;
    }

    @Override
    public double getDistance(List<Location> locations) {
        locations.sort(Comparator.comparing(Location::getTime));
        double distance = 0.0d;
        for (int i = locations.size() - 2, j = locations.size() - 1; i >= 0; i--, j--) {
            distance += DistanceCalculator.getDistance(
                    locations.get(i).getLat(),
                    locations.get(i).getLon(),
                    locations.get(j).getLat(),
                    locations.get(j).getLon());
        }
        return distance;
    }

    @Override
    public BigDecimal getTotalPriceRoutes(List<Route> routes) {
        BigDecimal price = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        for (Route route : routes) {
            price = price.add(route.getPrice());
        }
        return price;
    }

    @Override
    public double getTotalDistanceRoutes(List<Route> routes) {
        double distance = 0.0d;
        for (Route route : routes) {
            distance += route.getDistance();
        }
        return distance;
    }

    /**
     * Gets the difference in minutes between locations
     *
     * @param i The first location before j
     * @param j The second location after i
     * @return the difference between those locations in minutes.
     */
    @Override
    public long getTimeDifferenceMinutes(Location i, Location j) {
        Calendar timeI = i.getTime();
        Calendar timeJ = j.getTime();

        long timeDifference = timeJ.getTimeInMillis() - timeI.getTimeInMillis();
        long secondDifference = timeDifference / MILLIS_TO_SECONDS;
        long minuteDifference = secondDifference / SECONDS_TO_MINUTES;

        return minuteDifference;
    }

    @Override
    public boolean differenceIsGreaterOrEquals(long differenceMinutes, Location i, Location j) {
        return getTimeDifferenceMinutes(i, j) >= differenceMinutes;
    }

    @Override
    public List<EULocation> convertLocationsToEULocations(String license, List<Location> locations) {
        List<EULocation> euLocations = new ArrayList<>();
        if (locations != null) {
            for (Location l : locations) {
                long unixTime = l.getTime().getTimeInMillis() / 1000;
                euLocations.add(new EULocation(l.getLat(), l.getLon(), unixTime));
            }
        }
        // Sort the EULocations list
        euLocations.sort(EULocation::compareTo);
        return euLocations;
    }

    @Override
    public List<Location> convertEULocationsToLocation(String id, List<List<EULocation>> euLocations){
        List<Location> locations = new ArrayList<>();
        if(euLocations != null){
            for(List<EULocation> trip : euLocations){
                for(EULocation euLocation : trip) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(euLocation.getTimestamp() * 1000);
                    locations.add(new Location(
                            euLocation.getLat(),
                            euLocation.getLng(),
                            calendar));
                }
            }
        }
        locations.sort(Comparator.comparing(Location::getTime));
        return locations;
    }

    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }
}
