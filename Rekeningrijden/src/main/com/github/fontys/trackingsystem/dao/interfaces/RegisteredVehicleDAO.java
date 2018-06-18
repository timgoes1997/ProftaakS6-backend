package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.vehicle.RegisteredVehicle;

import java.util.List;

public interface RegisteredVehicleDAO {
    void create(RegisteredVehicle registeredVehicle);

    void edit(RegisteredVehicle registeredVehicle);

    void remove(RegisteredVehicle registeredVehicle);

    boolean exists(long id);

    RegisteredVehicle find(long id);

    RegisteredVehicle findByLicense(String license);

    List<RegisteredVehicle> findByUser(long id);

    RegisteredVehicle findByVehicle(long id);

    List<RegisteredVehicle> getAll();
}
