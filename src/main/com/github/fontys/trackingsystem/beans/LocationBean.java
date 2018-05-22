package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.tracking.Location;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@RequestScoped
@Path("/location")
public class LocationBean {

    @Inject
    private LocationService locationService;

    @POST
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/date")
    public List<Location> getVehicleOnLocation(@PathParam("license") String license,
                                               @FormParam("startdate") String startdate,
                                               @FormParam("enddate") String enddate) {
        return locationService.getVehicleOnLocation(license, startdate, enddate);
    }

    @POST
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/realtime")
    public List<Location> getVehicleOnLocation(@PathParam("license") String license) {
        return locationService.getVehicleOnLocation(license);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public void updateCurrentLocation(
            @FormParam("license") String license,
            @FormParam("lat") double lat,
            @FormParam("lon") double lon) {
        locationService.updateCurrentLocation(license, lat, lon);
//        return Response.ok().build();
//        return locationService.updateCurrentLocation(license, lat, lon);
    }
}
