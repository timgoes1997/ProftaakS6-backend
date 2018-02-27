package com.github.fontys.vehicle;

import com.github.fontys.payment.Bill;
import com.github.fontys.user.Customer;

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
