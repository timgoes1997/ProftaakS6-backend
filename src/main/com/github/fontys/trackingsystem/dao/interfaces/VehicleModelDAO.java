package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.vehicle.VehicleModel;

import java.util.List;

public interface VehicleModelDAO {
    void create(VehicleModel vehicleModel);

    void remove(VehicleModel vehicleModel);

    VehicleModel find(long id);

    List<VehicleModel> findAllModels();

    List<VehicleModel> findModelsByModelName(String modelName);

    List<VehicleModel> findModelsByEdition(String modelName);

    List<VehicleModel> findModelsByFuelType(String modelName);

    List<VehicleModel> findModelsByEnergyLabel(String modelName);
}
