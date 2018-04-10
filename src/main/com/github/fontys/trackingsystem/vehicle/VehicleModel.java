package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.EnergyLabel;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "VEHICLE_MODEL")
@NamedQueries({
        @NamedQuery(name=VehicleModel.FIND_ALL,
                query="SELECT v FROM VEHICLE_MODEL v"),
        @NamedQuery(name=VehicleModel.FIND_BYID,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.id=:id"),
        @NamedQuery(name=VehicleModel.FIND_BYNAME,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.modelName=:modelName"),
        @NamedQuery(name=VehicleModel.FIND_BYEDITION,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.edition=:edition"),
        @NamedQuery(name=VehicleModel.FIND_BYFUELTYPE,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.fuelType=:fuelType"),
        @NamedQuery(name=VehicleModel.FIND_BYENERGYLABEL,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.energyLabel=:energyLabel"),
        @NamedQuery(name=VehicleModel.FIND_BYNAMEANDEDITION,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.modelName=:modelName " +
                        "AND v.edition=:edition"),
        @NamedQuery(name=VehicleModel.FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL,
                query="SELECT v FROM VEHICLE_MODEL v WHERE v.modelName=:modelName " +
                        "AND v.edition=:edition AND v.fuelType=:fuelType AND v.energyLabel=:energyLabel"),
})
public class VehicleModel implements Serializable{

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "VehicleModel.findAll";
    public static final String FIND_BYID = "VehicleModel.findByID";
    public static final String FIND_BYNAME = "VehicleModel.findByName";
    public static final String FIND_BYEDITION = "VehicleModel.findByEdition";
    public static final String FIND_BYFUELTYPE = "VehicleModel.findByFuelType" ;
    public static final String FIND_BYENERGYLABEL = "VehicleModel.findByEnergyLabel";
    public static final String FIND_BYNAMEANDEDITION = "VehicleModel.findByNameAndEdition";
    public static final String FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL = "VehicleModel.find";

    // ======================================
    // =             Fields              =
    // ======================================


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
