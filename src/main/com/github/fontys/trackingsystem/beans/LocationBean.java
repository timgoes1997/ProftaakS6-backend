package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestScoped
@Path("/location")
public class LocationBean {

    @Inject
    private DatabaseMock db;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/date")
    public Response getVehicleOnLocation(@PathParam("license") String license, @FormParam("startdate") String startdate, @FormParam("enddate") String enddate) {

        // get the vehicle
        TrackedVehicle id = getTrackedVehicle(license);

        // if no vehicle, wrong license
        if (id == null) {
            return Response.status(200, "Could not find license plate").build();
        }

        // parse the time
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-mm-dd");
        Date start = null;
        Date end = null;

        // can't parse? Our fault
        try {
            start = parse.parse(startdate);
            end = parse.parse(enddate);
        } catch (ParseException e) {
            return Response.status(500, "Unknown date format").build();
        }

        // get the locations of the vehicle
        // todo
        List<Location> locations = new ArrayList<>();
        if (start.equals(end)) { // realtime
            locations.add(id.getLocation()); // Add last known location
        } else {
            locations.add(id.getLocation()); // not realtime
        }
        GenericEntity<List<Location>> list = new GenericEntity<List<Location>>(locations) {};
        return Response.ok(list).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/realtime")
    public Response getVehicleOnLocation(@PathParam("license") String license) {

        Calendar now = new GregorianCalendar();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd");
        String dateFormatted = fmt.format(now.getTime());
        return getVehicleOnLocation(license, dateFormatted, dateFormatted);
    }

    private TrackedVehicle getTrackedVehicle(String license) {
        /*
        List<TrackedVehicle> vehicleList = db.getTrackedVehicles();
        for (TrackedVehicle veh : vehicleList) {
            CustomerVehicle a = veh.getCustomerVehicle();
            if (a != null) {
                if (a.getLicensePlate().toLowerCase().equals(license.toLowerCase())) {
                    return veh;
                }
            }
        }*/
        return null;
    }
}
