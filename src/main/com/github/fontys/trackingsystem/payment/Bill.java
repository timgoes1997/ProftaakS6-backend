package com.github.fontys.trackingsystem.payment;

import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Bill implements Serializable{
    private CustomerVehicle customerVehicle;
    private Currency currency;
    private BigDecimal price;
    private BigDecimal alreadyPaid;
    private Date startDate;
    private Date endDate;
    private TrackedVehicle trackedVehicle;
    private PaymentStatus paymentStatus;
    private double mileage;

    public Bill(CustomerVehicle customerVehicle, Currency currency, BigDecimal price, BigDecimal alreadyPaid, Date startDate, Date endDate, TrackedVehicle trackedVehicle, PaymentStatus paymentStatus, double mileage) {
        this.customerVehicle = customerVehicle;
        this.currency = currency;
        this.price = price;
        this.alreadyPaid = alreadyPaid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trackedVehicle = trackedVehicle;
        this.paymentStatus = paymentStatus;
        this.mileage = mileage;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public TrackedVehicle getTrackedVehicle() {
        return trackedVehicle;
    }

    public void setTrackedVehicle(TrackedVehicle trackedVehicle) {
        this.trackedVehicle = trackedVehicle;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "customerVehicle=" + customerVehicle +
                ", currency=" + currency +
                ", price=" + price +
                ", alreadyPaid=" + alreadyPaid +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", trackedVehicle=" + trackedVehicle +
                ", paymentStatus=" + paymentStatus +
                ", mileage=" + mileage +
                '}';
    }
}
