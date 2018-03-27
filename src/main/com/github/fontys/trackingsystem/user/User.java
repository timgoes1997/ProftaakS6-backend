package com.github.fontys.trackingsystem.user;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="CUSTOMER")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="RESIDENCY")
    private String residency;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @OneToOne(fetch= FetchType.LAZY, mappedBy="user")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEPARTMENT")
    private Department department;

    @OneToMany(mappedBy="customer")
    private List<CustomerVehicle> customerVehicles;

    public User(String name, String address, String residency, Role role) {
        this.name = name;
        this.address = address;
        this.residency = residency;
        this.role = role;
    }

    public User(){}

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
