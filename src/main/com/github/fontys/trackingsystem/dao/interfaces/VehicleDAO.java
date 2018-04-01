package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;

import java.util.Date;
import java.util.List;

public interface VehicleDAO {
    void create(Vehicle vehicle);

    void edit(Vehicle vehicle);

    void remove(Vehicle vehicle);

    Vehicle find(long id);

    Vehicle find(String brand, Long vehicleModel, Date buildDate);

    List<Vehicle> findAll();

    List<Vehicle> findByModel(long id);

    List<Vehicle> findByBrand(String brand);
}
