package com.github.fontys.payment;

import com.github.fontys.tracking.Tracking;
import com.github.fontys.vehicle.CustomerVehicle;

import java.math.BigDecimal;
import java.util.Date;

public class Bill {
    private CustomerVehicle customerVehicle;
    private Currency currency;
    private BigDecimal price;
    private BigDecimal alreadyPaid;
    private Date startDate;
    private Date endDate;
    private Tracking tracking;
    private PaymentStatus paymentStatus;
    private double mileage;

    public Bill(CustomerVehicle customerVehicle, Currency currency, BigDecimal price, BigDecimal alreadyPaid, Date startDate, Date endDate, Tracking tracking, PaymentStatus paymentStatus, double mileage) {
        this.customerVehicle = customerVehicle;
        this.currency = currency;
        this.price = price;
        this.alreadyPaid = alreadyPaid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tracking = tracking;
        this.paymentStatus = paymentStatus;
        this.mileage = mileage;
    }
}
