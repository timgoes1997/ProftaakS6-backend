package com.github.fontys.trackingsystem.user;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String address;
    private String residency;
    private Role role;
    private Account account;
    private Department department;
    private List<CustomerVehicle> customerVehicles;

    public User(String name, String address, String residency, Role role) {
        this.name = name;
        this.address = address;
        this.residency = residency;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<CustomerVehicle> getCustomerVehicles() {
        return customerVehicles;
    }

    public void setCustomerVehicles(List<CustomerVehicle> customerVehicles) {
        this.customerVehicles = customerVehicles;
    }
}
