package com.github.fontys.trackingsystem.vehicle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "VEHICLE")
@NamedQueries({
        @NamedQuery(name="Vehicle.findAll",
                query="SELECT v FROM VEHICLE v"),
        @NamedQuery(name="Vehicle.findByID",
                query="SELECT v FROM VEHICLE v WHERE v.id=:id"),
        @NamedQuery(name="Vehicle.findByBrand",
                query="SELECT v FROM VEHICLE v WHERE v.brand=:brand"),
        @NamedQuery(name="Vehicle.findByModel",
                query="SELECT v FROM VEHICLE v WHERE v.vehicleModel.id=:id"),
        @NamedQuery(name="Vehicle.find",
                query="SELECT v FROM VEHICLE v WHERE v.vehicleModel.id=:id " +
                        "AND v.brand=:brand AND v.buildDate=:buildDate"),
 })
public class Vehicle implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @OneToOne(fetch = FetchType.LAZY)
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
