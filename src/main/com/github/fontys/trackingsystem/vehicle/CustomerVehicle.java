package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.user.Customer;

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
