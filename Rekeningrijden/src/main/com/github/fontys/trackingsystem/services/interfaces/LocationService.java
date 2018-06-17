package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.tracking.Location;

import java.util.Date;
import java.util.List;

public interface LocationService {
    List<Location> getVehicleOnLocationPrecise(String license, String startdate, String enddate);

    List<Location> getLocationsBetweenDatesByVehicleLicense(String license, String startdate, String enddate);
    List<Location> getLocationsBetweenTimesByVehicleLicense(String license, String startdate, String enddate);
    List<Location> getLocationsBetweenTimesByVehicleLicense(String license, Date startdate, Date enddate);

    List<Location> getVehicleOnLocation(String license, String startdate, String enddate);
    List<Location> getVehicleOnLocation(String license);
    boolean isAuthorisedToTrack(String license);

    void updateCurrentLocation(String license, double lat, double lon);
}
