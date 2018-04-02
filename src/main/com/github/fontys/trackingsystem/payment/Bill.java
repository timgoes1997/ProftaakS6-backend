package com.github.fontys.trackingsystem.payment;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(name="BILL")
@NamedQueries({
        @NamedQuery(name = "Bill.findByID",
                query = "SELECT b FROM BILL b WHERE b.id=:id"),

        @NamedQuery(name = "Bill.findByVehicleId",
                query = "SELECT b FROM BILL b WHERE b.customerVehicle.id=:id"),
        // TODO: 2-4-18 Idk of je zo bij een nested class kan met jpa queries

        @NamedQuery(name = "Bill.findByStatus",
                query = "SELECT b FROM BILL b WHERE b.status=:status"),
})
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CUSTOMER_VEHICLE_ID")
    private CustomerVehicle customerVehicle;

    @Column(name="PRICE")
    private BigDecimal price;

    @Column(name="ALREADY_PAID")
    private BigDecimal alreadyPaid;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Calendar startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Calendar endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS")
    private PaymentStatus status;

    @Column(name="MILEAGE")
    private double mileage;

    public Bill() {}

    public Bill(CustomerVehicle customerVehicle, Currency currency, BigDecimal price, BigDecimal alreadyPaid, Calendar startDate, Calendar endDate, PaymentStatus paymentStatus, double mileage) {
        this.customerVehicle = customerVehicle;
        this.price = price;
        this.alreadyPaid = alreadyPaid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = paymentStatus;
        this.mileage = mileage;
    }

    @XmlTransient
    public CustomerVehicle getCustomerVehicle() {
        return customerVehicle;
    }

    public void setCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicle = customerVehicle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(BigDecimal alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @XmlAttribute
    public String getLicense() {
        return customerVehicle.getLicensePlate();
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }
    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    @XmlAttribute
    public String getMonth() {
        return new SimpleDateFormat("MMM").format(startDate.getTime());
    }

    public Long getId() {
        return id;
    }
}
