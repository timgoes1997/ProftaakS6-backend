package com.github.fontys;

import com.github.fontys.user.Customer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomerVehicle {
    private Customer customer;
    private String licensePlate;
    private Vehicle vehicle;
    private String proofOfOwnership;
    private List<Bill> bills;

    public CustomerVehicle(Customer customer, String licensePlate, Vehicle vehicle, String proofOfOwnership) {
        this.customer = customer;
        this.licensePlate = licensePlate;
        this.vehicle = vehicle;
        this.proofOfOwnership = proofOfOwnership;
    }
}
