package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LocationServiceImpl implements LocationService {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private TrackedVehicleDAO trackedVehicleDAO;

    @Override
    public List<Location> getVehicleOnLocation(String license, String startdate, String enddate) {

        // only admins are allowed to backtrack all
        if (!isAuthorisedToTrack(license)) {
            throw new NotAuthorizedException("Not allowed to track unowned vehicle");
        }


        // Not realtime
        // Parse the time
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;

        // can't parse? Our fault
        try {
            start = parse.parse(startdate);
            end = parse.parse(enddate);
        } catch (ParseException e) {
            throw new BadRequestException();
        }

        List<Location> locations;

        // Get the vehicle ID, then get all locations with the vehicle with that ID
        RegisteredVehicle rv = registeredVehicleDAO.findByLicense(license);
        // Get all locations of the vehicle with retrieved vehicle ID
        locations = trackedVehicleDAO.findLocationsByRegisteredVehicleID(rv.getId());

        locations.removeAll(Collections.singleton(null));

        // filter the map on date, if the map is not empty
        if (!locations.isEmpty()) {
            Iterator<Location> locIter = locations.iterator();
            while (locIter.hasNext()) {
                Location l = locIter.next();
                Date cl = l.getTime().getTime();
                // if the date of the location falls outside the specified dates, remove the location
                if (l.getTime().getTime().before(start) || l.getTime().getTime().after(end)) {
                    locIter.remove();
                }
            }
        }

        //Sort the list by dates
        Collections.sort(locations, new Comparator<Location>() {
            public int compare(Location o1, Location o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        return locations;
    }

    @Override
    public List<Location> getVehicleOnLocation(String license) {
        if (currentUser.getPrivilege() != Role.POLICE_EMPLOYEE) { // police is always allowed to track realtime
            if (!isAuthorisedToTrack(license)) {
                throw new NotAuthorizedException("Not allowed to track unowned vehicle");
            }
        }
        List<Location> locations = new ArrayList<>();

        RegisteredVehicle rv = registeredVehicleDAO.findByLicense(license);

        if (rv == null) {
            return locations;
        }

        TrackedVehicle tv = trackedVehicleDAO.findByRegisteredVehicleID(rv.getId());

        locations.add(tv.getLastLocation()); // Add last known location
        return locations;
    }

    @Override
    public boolean isAuthorisedToTrack(String license) {
        RegisteredVehicle vehicle;
        try {
            vehicle = registeredVehicleDAO.findByLicense(license);
        } catch (NoResultException nr) {
            return false; // not authorised to know if a car doesn't exist
        }

        if (vehicle.getCustomer().getId() == ((User) currentUser).getId()) {
            return true;
        }
        return false;
    }
}