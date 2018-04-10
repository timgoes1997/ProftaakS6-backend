package com.github.fontys.trackingsystem.vehicle;


import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="CUSTOMER_VEHICLE")
@NamedQueries({
        @NamedQuery(name = CustomerVehicle.FIND_ALL,
                query = "SELECT c FROM CUSTOMER_VEHICLE c"),
        @NamedQuery(name = CustomerVehicle.FIND_BYID,
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.id=:id"),
        @NamedQuery(name = CustomerVehicle.FIND_BYUSER,
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.customer.id=:id"),
        @NamedQuery(name = CustomerVehicle.FIND_BYLICENSEPLATE,
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.licensePlate=:licensePlate"),
        @NamedQuery(name = CustomerVehicle.FIND_BYVEHICLE,
                query = "SELECT c FROM CUSTOMER_VEHICLE c WHERE c.vehicle.id=:id"),
})
public class CustomerVehicle implements Serializable {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "CustomerVehicle.findAll";
    public static final String FIND_BYID = "CustomerVehicle.findByID";
    public static final String FIND_BYUSER = "CustomerVehicle.findByUser";
    public static final String FIND_BYLICENSEPLATE = "CustomerVehicle.findByLicense";
    public static final String FIND_BYVEHICLE = "CustomerVehicle.findByVehicle";

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="OWNER_ID")
    private User customer;

    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="VEHICLE_ID")
    private Vehicle vehicle;

    @Column(name="PROOF_OF_OWNERSHIP")
    private String proofOfOwnership;

    @OneToMany(mappedBy="customerVehicle", cascade = CascadeType.PERSIST)
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
