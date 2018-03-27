package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.user.User;

import java.io.Serializable;
import java.util.List;

public class CustomerVehicle implements Serializable {

    private int id;
    private User customer;
    private String licensePlate;
    private Vehicle vehicle;
    private String proofOfOwnership;
    private List<Bill> bills;

    public CustomerVehicle(int id, User customer, String licensePlate, Vehicle vehicle, String proofOfOwnership) {
        this.id = id;
        this.customer = customer;
        this.licensePlate = licensePlate;
        this.vehicle = vehicle;
        this.proofOfOwnership = proofOfOwnership;
    }

    public CustomerVehicle(User customer, String licensePlate, Vehicle vehicle, String proofOfOwnership) {
        this.customer = customer;
        this.licensePlate = licensePlate;
        this.vehicle = vehicle;
        this.proofOfOwnership = proofOfOwnership;
    }

    @Override
    public String toString() {
        return String.format("CV: id %s, licensePlate %s", id, licensePlate);
//        return super.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getProofOfOwnership() {
        return proofOfOwnership;
    }

    public void setProofOfOwnership(String proofOfOwnership) {
        this.proofOfOwnership = proofOfOwnership;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
