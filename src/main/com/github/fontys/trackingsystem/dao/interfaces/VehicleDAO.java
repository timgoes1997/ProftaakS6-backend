package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

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

    List<String> getBrands();


    Vehicle find(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel);

    List<Vehicle> findModelsByModelName(String modelName);

    List<Vehicle> findModelsByEdition(String modelName);

    List<Vehicle> findModelsByNameAndEdition(String modelName, String edition);

    List<Vehicle> findModelsByFuelType(FuelType fuelType);

    List<Vehicle> findModelsByEnergyLabel(EnergyLabel energyLabel);
}
