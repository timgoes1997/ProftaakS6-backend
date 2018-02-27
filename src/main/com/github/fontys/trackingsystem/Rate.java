package com.github.fontys.trackingsystem;

import com.github.fontys.payment.Currency;

import java.math.BigDecimal;
import java.util.Date;

public class Rate {
    private Region region;
    private Currency currency;
    private BigDecimal kilometerPrice;
    // possible extension; add employee who assigned this rate
    private EnergyLabel energyLabel;
    private Date startTime;
    private Date endTime;

    public Rate(Region region, Currency currency, BigDecimal kilometerPrice, EnergyLabel energyLabel, Date startTime, Date endTime) {
        this.region = region;
        this.currency = currency;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
