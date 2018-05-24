package com.github.fontys.trackingsystem.tracking;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name="TRACKED_VEHICLE")
@NamedQueries({
        @NamedQuery(name = TrackedVehicle.FIND_LOCATIONSBYTRACKEDVEHICLEID,
                query = "SELECT tv.locations FROM TRACKED_VEHICLE tv WHERE tv.id=:id"),
        @NamedQuery(name = TrackedVehicle.FIND_LOCATIONSBYREGISTEREDVEHICLEID,
                query = "SELECT tv.locations FROM TRACKED_VEHICLE tv WHERE tv.registeredVehicle.id=:id"),
        @NamedQuery(name = TrackedVehicle.FIND_TRACKEDVEHICLEBYCUSTOMERVEHICLEID,
                query = "SELECT tv FROM TRACKED_VEHICLE tv WHERE tv.registeredVehicle.id=:id"),
        @NamedQuery(name = TrackedVehicle.FIND_LASTLOCATIONBYTRACKEDVEHICLEID,
        query = "SELECT tv.lastLocation FROM TRACKED_VEHICLE tv WHERE tv.id=:id"),
})

public class TrackedVehicle implements Serializable {

    public static final String FIND_LOCATIONSBYTRACKEDVEHICLEID = "TrackedVehicle.findLocationsByTrackedVehicleID";
    public static final String FIND_LOCATIONSBYREGISTEREDVEHICLEID = "TrackedVehicle.findLocationsByRegisteredVehicleID";
    public static final String FIND_TRACKEDVEHICLEBYCUSTOMERVEHICLEID = "TrackedVehicle.findTrackedVehicleByCustomerVehicleID";
    public static final String FIND_LASTLOCATIONBYTRACKEDVEHICLEID = "TrackedVehicle.findLastLocationByTrackedVehicleID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="REGISTERED_VEHICLE", unique = true)
    private RegisteredVehicle registeredVehicle;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="LAST_LOCATION")
    private Location lastLocation;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "TRACKEDVEHICLE_LOCATIONS",
            joinColumns = { @JoinColumn(name="TRACKEDVEHICLE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")})
    private Set<Location> locations; // must be ordered consecutively

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="HARDWARE")
    private Hardware hardware;

    public TrackedVehicle(){}
    public TrackedVehicle(RegisteredVehicle registeredVehicle, Location lastLocation, Hardware hardware) {
        this.registeredVehicle = registeredVehicle;
        this.hardware = hardware;
        this.lastLocation = lastLocation;
        this.locations = new HashSet<>();
    }

    public RegisteredVehicle getRegisteredVehicle() {
        return registeredVehicle;
    }

    public void setRegisteredVehicle(RegisteredVehicle registeredVehicle) {
        this.registeredVehicle = registeredVehicle;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public synchronized void setLastLocation(Location lastLocation) {
        if (this.lastLocation != null) {
            getLocations().add(this.lastLocation);
        }
        this.lastLocation = lastLocation;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
}
