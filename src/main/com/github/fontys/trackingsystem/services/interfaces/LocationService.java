package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.tracking.Location;

import javax.ws.rs.PathParam;
import java.util.List;

public interface LocationService {
    List<Location> getVehicleOnLocation(String license, String startdate, String enddate);
    List<Location> getVehicleOnLocation(String license);
    boolean isAuthorisedToTrack(String license);
}
