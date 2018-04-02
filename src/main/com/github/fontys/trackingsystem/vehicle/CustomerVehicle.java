package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="CUSTOMER_VEHICLE")
@NamedQueries({
        @NamedQuery(name = "CustomerVehicle.findByID",
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.id=:id"),
        @NamedQuery(name = "CustomerVehicle.findByUser",
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.customer.id=:id"),
        @NamedQuery(name = "CustomerVehicle.findByLicense",
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.licensePlate=:licensePlate"),
        @NamedQuery(name = "CustomerVehicle.findByVehicle",
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.vehicle.id=:id"),
})
public class CustomerVehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    private User customer;

    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @OneToOne
    @JoinColumn(name="VEHICLE_ID")
    private Vehicle vehicle;

    @Column(name="PROOF_OF_OWNERSHIP")
    private String proofOfOwnership;

    @OneToMany(mappedBy="customerVehicle")
    private List<Bill> bills;

    public CustomerVehicle(Long id, User customer, String licensePlate, Vehicle vehicle, String proofOfOwnership) {
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

    public CustomerVehicle(){}

    @Override
    public String toString() {
        return String.format("CV: id %s, licensePlate %s", id, licensePlate);
//        return super.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    /*
    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }*/
}
