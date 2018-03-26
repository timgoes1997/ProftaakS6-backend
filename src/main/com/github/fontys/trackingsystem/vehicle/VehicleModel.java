package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.EnergyLabel;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "VEHICLE_MODEL")
public class VehicleModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODELNAME")
    private String modelName;

    @Column(name = "EDITION")
    private String edition;

    @Enumerated(EnumType.STRING)
    @Column(name = "FUELTYPE")
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENERGYLABEL")
    private EnergyLabel energyLabel;

    public VehicleModel(Long id, String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel) {
        this.id = id;
        this.modelName = modelName;
        this.edition = edition;
        this.fuelType = fuelType;
        this.energyLabel = energyLabel;
    }

    public VehicleModel(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel) {
        this.modelName = modelName;
        this.edition = edition;
        this.fuelType = fuelType;
        this.energyLabel = energyLabel;
    }

    public VehicleModel() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
