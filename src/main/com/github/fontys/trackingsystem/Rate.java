package com.github.fontys.trackingsystem;


import com.github.fontys.trackingsystem.payment.Currency;
import com.github.fontys.trackingsystem.region.Region;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "RATE")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 2)
    @Column(name = "KILOMETER_PRICE")
    private BigDecimal kilometerPrice;

    // possible extension; add employee who assigned this rate

    @Enumerated(EnumType.STRING)
    @Column(name = "ENERGYLABEL")
    private EnergyLabel energyLabel;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Date startTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Date endTime;

    public Rate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Date startTime, Date endTime) {
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Rate() {
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BigDecimal getKilometerPrice() {
        return kilometerPrice;
    }

    public void setKilometerPrice(BigDecimal kilometerPrice) {
        this.kilometerPrice = kilometerPrice;
    }

    public EnergyLabel getEnergyLabel() {
        return energyLabel;
    }

    public void setEnergyLabel(EnergyLabel energyLabel) {
        this.energyLabel = energyLabel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }
}
