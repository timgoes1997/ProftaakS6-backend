package com.github.fontys.trackingsystem.vehicle;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable{
    private String brand;
    private VehicleModel vehicleModel;
    private Date buildDate;

    public Vehicle(String brand, VehicleModel vehicleModel, Date buildDate) {
        this.brand = brand;
        this.vehicleModel = vehicleModel;
        this.buildDate = buildDate;
    }
}
