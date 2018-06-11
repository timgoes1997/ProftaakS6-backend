package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.tracking.Location;

public interface LocationDAO {

    void create(Location location);

    void edit(Location location);

    void remove(Location location);

    Location findByID(long id);
}