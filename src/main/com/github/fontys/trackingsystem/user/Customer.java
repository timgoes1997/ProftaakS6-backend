package com.github.fontys.trackingsystem.user;


import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import java.io.Serializable;
import java.util.List;

public class Customer extends User implements Serializable{
    private List<CustomerVehicle> customerVehicles;

    public Customer(String name, String address, String residency, Role role) {
        super(name, address, residency, role);
    }

    public List<CustomerVehicle> getCustomerVehicles() {
        return customerVehicles;
    }

    public void setCustomerVehicles(List<CustomerVehicle> customerVehicles) {
        this.customerVehicles = customerVehicles;
    }
}
