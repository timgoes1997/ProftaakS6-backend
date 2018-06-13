package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.region.Region;

import java.util.List;

public interface RegionDAO {
    void create(Region region);
    void edit(Region region);
    void remove(Region region);

    boolean exists(String name);
    boolean exists(long id);
    Region find(long id);
    Region find(String name);
    List<Region> getAllRegions();
}
