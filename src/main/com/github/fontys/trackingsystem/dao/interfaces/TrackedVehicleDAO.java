package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.tracking.TrackedVehicle;

import java.util.List;

public interface TrackedVehicleDAO {

    void create(TrackedVehicle trackedVehicle);

    void edit(TrackedVehicle trackedVehicle);

    void remove(TrackedVehicle trackedVehicle);

    TrackedVehicle findByID(long id);
}
