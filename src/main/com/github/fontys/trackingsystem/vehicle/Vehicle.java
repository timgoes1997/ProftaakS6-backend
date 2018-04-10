package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.trackingsystem.EnergyLabel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "VEHICLE")
@NamedQueries({
        @NamedQuery(name= Vehicle.FIND_ALL,
                query="SELECT v FROM VEHICLE v"),
        @NamedQuery(name=Vehicle.FIND_BYID,
                query="SELECT v FROM VEHICLE v WHERE v.id=:id"),
        @NamedQuery(name=Vehicle.FIND_BYBRAND,
                query="SELECT v FROM VEHICLE v WHERE v.brand=:brand"),
        @NamedQuery(name=Vehicle.FIND_BYMODEL,
                query="SELECT v FROM VEHICLE v WHERE v.vehicleModel.id=:id"),
        @NamedQuery(name=Vehicle.FIND_BYMODELBRANDBUILDDATE,
                query="SELECT v FROM VEHICLE v WHERE v.vehicleModel.id=:id " +
                        "AND v.brand=:brand AND v.buildDate=:buildDate"),

        @NamedQuery(name=Vehicle.FIND_BYNAME,
                query="SELECT v FROM VEHICLE v WHERE v.modelName=:modelName"),
        @NamedQuery(name=Vehicle.FIND_BYEDITION,
                query="SELECT v FROM VEHICLE v WHERE v.edition=:edition"),
        @NamedQuery(name=Vehicle.FIND_BYFUELTYPE,
                query="SELECT v FROM VEHICLE v WHERE v.fuelType=:fuelType"),
        @NamedQuery(name=Vehicle.FIND_BYENERGYLABEL,
                query="SELECT v FROM VEHICLE v WHERE v.energyLabel=:energyLabel"),
        @NamedQuery(name=Vehicle.FIND_BYNAMEANDEDITION,
                query="SELECT v FROM VEHICLE v WHERE v.modelName=:modelName " +
                        "AND v.edition=:edition"),
        @NamedQuery(name=Vehicle.FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL,
                query="SELECT v FROM VEHICLE v WHERE v.modelName=:modelName " +
                        "AND v.edition=:edition AND v.fuelType=:fuelType AND v.energyLabel=:energyLabel"),

 })
public class Vehicle implements Serializable{
    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "Vehicle.findAll";
    public static final String FIND_BYID = "Vehicle.findByID";
    public static final String FIND_BYBRAND = "Vehicle.findByBrand";
    public static final String FIND_BYMODEL = "Vehicle.findByModel";
    public static final String FIND_BYMODELBRANDBUILDDATE = "Vehicle.findModelBrandDate";

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

    @Column(name="BRAND")
    private String brand;

    @Temporal(TemporalType.DATE)
    @Column(name = "BUILD_DATE")
    private Date buildDate;

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

    public Vehicle(String brand, Date buildDate) {
        this.brand = brand;
        this.buildDate = buildDate;
    }

    public Vehicle() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public Long getId() {
        return id;
    }
}
