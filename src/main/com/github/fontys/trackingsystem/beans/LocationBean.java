package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.tracking.Location;

import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
