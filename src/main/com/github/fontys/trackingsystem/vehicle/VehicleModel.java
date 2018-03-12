package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.EnergyLabel;

import java.io.Serializable;

public class VehicleModel implements Serializable{
    private int id;
    private String modelName;
    private String edition;
    private FuelType fuelType;
    private EnergyLabel energyLabel;

    public VehicleModel(int id, String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel) {
        this.id = id;
        this.modelName = modelName;
        this.edition = edition;
        this.fuelType = fuelType;
        this.energyLabel = energyLabel;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public EnergyLabel getEnergyLabel() {
        return energyLabel;
    }

    public void setEnergyLabel(EnergyLabel energyLabel) {
        this.energyLabel = energyLabel;
    }
}
