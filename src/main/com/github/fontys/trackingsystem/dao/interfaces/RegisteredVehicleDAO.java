package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import java.util.List;

public interface RegisteredVehicleDAO {
    void create(RegisteredVehicle registeredVehicle);

    void edit(RegisteredVehicle registeredVehicle);

    void remove(RegisteredVehicle registeredVehicle);

    RegisteredVehicle find(long id);

    RegisteredVehicle findByLicense(String license);

    List<RegisteredVehicle> findByUser(long id);

    List<RegisteredVehicle> findByVehicle(long id);

    List<RegisteredVehicle> getAll();
}
