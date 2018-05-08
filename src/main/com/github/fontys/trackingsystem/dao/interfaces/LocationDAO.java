package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;

import java.util.List;

public interface LocationDAO {

    void create(Location location);

    void edit(Location location);

    void remove(Location location);

    Location findByID(long id);
}