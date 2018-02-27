package com.github.fontys.user;

import com.github.fontys.vehicle.CustomerVehicle;

import java.util.List;

public class Customer extends User{
    private List<CustomerVehicle> customerVehicles;

    public Customer(String name, String address, String residency, Role role) {
        super(name, address, residency, role);
    }
}
