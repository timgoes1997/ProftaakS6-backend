package com.github.fontys.trackingsystem.tracking;


import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="TRACKED_VEHICLE")
public class TrackedVehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="CUSTOMER_VEHICLE")
    private RegisteredVehicle registeredVehicle;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="LOCATION")
    private Location location;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="HARDWARE")
    private Hardware hardware;

    public TrackedVehicle(){}

    public TrackedVehicle(RegisteredVehicle registeredVehicle, Location location, Hardware hardware) {
        this.registeredVehicle = registeredVehicle;
        this.location = location;
        this.hardware = hardware;
    }

    public RegisteredVehicle getRegisteredVehicle() {
        return registeredVehicle;
    }

    public void setRegisteredVehicle(RegisteredVehicle registeredVehicle) {
        this.registeredVehicle = registeredVehicle;
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
