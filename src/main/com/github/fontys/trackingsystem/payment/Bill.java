package com.github.fontys.trackingsystem.payment;

import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Bill implements Serializable {
    private int billnr;
    private CustomerVehicle customerVehicle;
    private Currency currency;
    private BigDecimal price;
    private BigDecimal alreadyPaid;
    private Calendar startDate;
    private Calendar endDate;
    private PaymentStatus status;
    private double mileage;

    /*
        For reflective code
     */
    @Deprecated
    public Bill() {}

    public Bill(CustomerVehicle customerVehicle, Currency currency, BigDecimal price, BigDecimal alreadyPaid, Calendar startDate, Calendar endDate, PaymentStatus paymentStatus, double mileage) {
        this.customerVehicle = customerVehicle;
        this.currency = currency;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public int getBillnr() {
        return billnr;
    }

    public void setBillnr(int billnr) {
        this.billnr = billnr;
    }
}
