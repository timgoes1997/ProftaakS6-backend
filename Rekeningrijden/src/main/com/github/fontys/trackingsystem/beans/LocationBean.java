package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.entities.tracking.Location;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateful
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
    @Path("/{license}/precise")
    public List<Location> getVehicleOnLocationInt(@PathParam("license") String license,
                                               @FormParam("startdate") Long startdate,
                                               @FormParam("enddate") Long enddate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // int to dates
        Date start = new Date(startdate);
        Date end = new Date(enddate);

        // todo: migrate to function to make faster
        return locationService.getVehicleOnLocationPrecise(license, format.format(start), format.format(end));
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
