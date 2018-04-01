package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

public interface CustomerVehicleDAO {
    void create(CustomerVehicle customerVehicle);

    void edit(CustomerVehicle customerVehicle);

    void remove(CustomerVehicle customerVehicle);

    CustomerVehicle find(long id);

    CustomerVehicle findByUser(long id);

    CustomerVehicle findByVehicle(long id);
}
