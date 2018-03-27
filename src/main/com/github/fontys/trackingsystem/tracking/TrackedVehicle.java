package com.github.fontys.trackingsystem.tracking;


import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.persistence.Entity;
import java.io.Serializable;

//@Entity(name="TRACKED_VEHICLE")
public class TrackedVehicle implements Serializable {

    private CustomerVehicle customerVehicle;
    private Location location;
    private Hardware hardware;

    public TrackedVehicle(){}

    public TrackedVehicle(CustomerVehicle customerVehicle, Location location, Hardware hardware) {
        this.customerVehicle = customerVehicle;
        this.location = location;
        this.hardware = hardware;
    }

    public CustomerVehicle getCustomerVehicle() {
        return customerVehicle;
    }

    public void setCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicle = customerVehicle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }
}
