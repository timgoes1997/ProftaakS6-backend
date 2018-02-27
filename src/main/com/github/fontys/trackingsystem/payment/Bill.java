package com.github.fontys.trackingsystem.payment;

import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import java.math.BigDecimal;
import java.util.Date;

public class Bill {
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
}
