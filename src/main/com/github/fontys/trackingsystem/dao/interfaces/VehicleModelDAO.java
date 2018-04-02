package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;

import java.util.List;

public interface VehicleModelDAO {
    void create(VehicleModel vehicleModel);

    void edit(VehicleModel vehicleModel);

    void remove(VehicleModel vehicleModel);

    VehicleModel find(long id);

    VehicleModel find(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel);

    List<VehicleModel> findAllModels();

    List<VehicleModel> findModelsByModelName(String modelName);

    List<VehicleModel> findModelsByEdition(String modelName);

    List<VehicleModel> findModelsByNameAndEdition(String modelName, String edition);

    List<VehicleModel> findModelsByFuelType(FuelType fuelType);

    List<VehicleModel> findModelsByEnergyLabel(EnergyLabel energyLabel);
}
