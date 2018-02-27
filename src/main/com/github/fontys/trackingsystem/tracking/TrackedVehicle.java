package com.github.fontys.trackingsystem.tracking;

import com.github.fontys.vehicle.CustomerVehicle;

public class TrackedVehicle {
    private CustomerVehicle customerVehicle;
    private Location location;
    private Hardware hardware;

    public TrackedVehicle(CustomerVehicle customerVehicle, Location location, Hardware hardware) {
        this.customerVehicle = customerVehicle;
        this.location = location;
        this.hardware = hardware;
    }
}
