package com.github.fontys.trackingsystem.vehicle;

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

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "VEHICLE_MODEL")
    private VehicleModel vehicleModel;

    @Temporal(TemporalType.DATE)
    @Column(name = "BUILD_DATE")
    private Date buildDate;

    public Vehicle(String brand, VehicleModel vehicleModel, Date buildDate) {
        this.brand = brand;
        this.vehicleModel = vehicleModel;
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

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
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
