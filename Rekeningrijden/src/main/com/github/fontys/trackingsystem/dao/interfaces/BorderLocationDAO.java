package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.region.BorderLocation;

public interface BorderLocationDAO {

    void create(BorderLocation location);

    void edit(BorderLocation location);

    void remove(BorderLocation location);

    BorderLocation findByID(long id);

}
