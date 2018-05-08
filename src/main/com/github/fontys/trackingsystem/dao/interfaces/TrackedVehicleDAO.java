package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;

import javax.sound.midi.Track;
import java.util.List;

public interface TrackedVehicleDAO {

    void create(TrackedVehicle trackedVehicle);

    void edit(TrackedVehicle trackedVehicle);

    void remove(TrackedVehicle trackedVehicle);

    TrackedVehicle findByID(long id);

    TrackedVehicle findByRegisteredVehicleID(long id);

    List<Location> findLocationsByTrackedVehicleID(long trackedVehicleID);

    List<Location> findLocationsByRegisteredVehicleID(long customerVehicleID);

    Location findLastLocationByTrackedVehicleID(long trackedVehicleID);
}