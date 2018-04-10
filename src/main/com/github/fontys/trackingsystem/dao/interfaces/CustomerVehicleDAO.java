package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import java.util.List;

public interface CustomerVehicleDAO {
    void create(CustomerVehicle customerVehicle);

    void edit(CustomerVehicle customerVehicle);

    void remove(CustomerVehicle customerVehicle);

    CustomerVehicle find(long id);

    CustomerVehicle findByLicense(String license);

    List<CustomerVehicle> findByUser(long id);

    List<CustomerVehicle> findByVehicle(long id);

    List<CustomerVehicle> getAll();
}
