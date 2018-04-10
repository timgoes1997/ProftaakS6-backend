package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.dao.interfaces.CustomerVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.LocationDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import org.glassfish.jersey.internal.util.collection.Value;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
@Path("/location")
public class LocationBean {

    @Inject
    private DatabaseMock db;

    @Inject
    private LocationDAO locationDAO;

    @Inject
    CustomerVehicleDAO customerVehicleDAO;

    @Inject
    TrackedVehicleDAO trackedVehicleDAO;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/date")
    public Response getVehicleOnLocation(@PathParam("license") String license, @FormParam("startdate") String startdate, @FormParam("enddate") String enddate) {

        // get the vehicle
        TrackedVehicle trackedVehicle = getTrackedVehicle(license);

        // if no vehicle, wrong license
        if (trackedVehicle == null) {
            return Response.status(200, "Could not find license plate").build();
        }

        List<Location> locations = new ArrayList<>();

        // Not realtime
        // Parse the time
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

        // Get the vehicle ID, then get all locations with the vehicle with that ID
        CustomerVehicle cv = customerVehicleDAO.findByLicense(license);
        locations = trackedVehicleDAO.findLocationsByCustomerVehicleID(cv.getId());

        // filter the map on date, if the map is not empty
        if (locations.size() > 0) {

            // assign iterator to iterate through map
            Iterator it = locations.iterator();

            //todo Change to normal foreach loop (was HashMap, now List)
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Location entryLocation = (Location) entry.getValue();

                // compare date
                if (entryLocation.getTime().after(start) && entryLocation.getTime().before(end)) {

                    // if the date of the location falls between the specified dates, add the location
                    locations.add(entryLocation);
                }
            }
        }

        GenericEntity<List<Location>> list = new GenericEntity<List<Location>>(locations) {
        };
        return Response.ok(list).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}/realtime")
    public Response getVehicleOnLocation(@PathParam("license") String license) {

        // get the vehicle
        TrackedVehicle trackedVehicle = getTrackedVehicle(license);

        // if no vehicle, wrong license
        if (trackedVehicle == null) {
            return Response.status(200, "Could not find license plate").build();
        }

        List<Location> locations = new ArrayList<>();
        locations.add(trackedVehicle.getLocation()); // Add last known location

        GenericEntity<List<Location>> list = new GenericEntity<List<Location>>(locations) {
        };
        return Response.ok(list).build();
    }

    private TrackedVehicle getTrackedVehicle(String license) {
        /*List<TrackedVehicle> vehicleList = db.getTrackedVehicles();
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
