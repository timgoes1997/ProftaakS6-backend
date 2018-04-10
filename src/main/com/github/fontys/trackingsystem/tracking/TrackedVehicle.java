package com.github.fontys.trackingsystem.tracking;


import com.github.fontys.trackingsystem.region.BorderLocation;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="TRACKED_VEHICLE")
@NamedQueries({
        @NamedQuery(name = TrackedVehicle.FIND_LOCATIONSBYTRACKEDVEHICLEID,
                query = "SELECT tv.locations FROM TRACKED_VEHICLE tv WHERE tv.id=:id"),
        @NamedQuery(name = TrackedVehicle.FIND_LOCATIONSBYCUSTOMERVEHICLEID,
                query = "SELECT tv.locations FROM TRACKED_VEHICLE tv WHERE tv.customerVehicle.id=:id"),
})
public class TrackedVehicle implements Serializable {

    public static final String FIND_LOCATIONSBYTRACKEDVEHICLEID = "TrackedVehicle.findLocationSByTrackedVehicleID";
    public static final String FIND_LOCATIONSBYCUSTOMERVEHICLEID = "TrackedVehicle.findLocationSByCustomerVehicleID";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name="CUSTOMER_VEHICLE")
    private CustomerVehicle customerVehicle;

    @OneToOne
    @JoinColumn(name="LAST_LOCATION")
    private Location lastLocation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TRACKEDVEHICLE_LOCATIONS",
            joinColumns = { @JoinColumn(name="TRACKEDVEHICLE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")})
    private List<Location> locations; // must be ordered consecutively

    @OneToOne
    @JoinColumn(name="HARDWARE")
    private Hardware hardware;

    public TrackedVehicle(){}

    public TrackedVehicle(CustomerVehicle customerVehicle, Location location, Hardware hardware) {
        this.customerVehicle = customerVehicle;
        this.lastLocation = location;
        this.hardware = hardware;
    }

    public CustomerVehicle getCustomerVehicle() {
        return customerVehicle;
    }

    public void setCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicle = customerVehicle;
    }

    public Location getLocation() {
        return lastLocation;
    }

    public void setLocation(Location location) {
        this.lastLocation = location;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }
}
