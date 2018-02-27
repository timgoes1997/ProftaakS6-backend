package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.EnergyLabel;

public class VehicleModel {
    private String modelName;
    private String edition;
    private FuelType fuelType;
    private EnergyLabel energyLabel;

    public VehicleModel(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel) {
        this.modelName = modelName;
        this.edition = edition;
        this.fuelType = fuelType;
        this.energyLabel = energyLabel;
    }
}
