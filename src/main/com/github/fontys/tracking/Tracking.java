package com.github.fontys.tracking;

import com.github.fontys.vehicle.CustomerVehicle;

public class Tracking {
    private CustomerVehicle customerVehicle;
    private Location location;
    private Hardware hardware;

    public Tracking(CustomerVehicle customerVehicle, Location location, Hardware hardware) {
        this.customerVehicle = customerVehicle;
        this.location = location;
        this.hardware = hardware;
    }
}
