package com.github.fontys.trackingsystem.mock;


import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Customer;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;

@Singleton
@Startup
public class DatabaseMock {

    public List<TrackedVehicle> vehicles;


    @PostConstruct
    void init(){
        System.out.println("Hello");
        for (int i = 0; i < 10; i++) {
            CustomerVehicle veh = new CustomerVehicle(1, new Customer("customer" + String.valueOf(i), "Address"), String licensePlate, Vehicle vehicle, String proofOfOwnership)
        }

    }

}
