package com.github.fontys.trackingsystem.tracking;

import com.github.fontys.trackingsystem.region.Region;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @Column(name="TRIP_REGION")
    private Region region;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name="DISTANCE")
    private double distanceInKilometers;

    @Column(name="DRIVEN_DATE")
    private Date date;

    @Column(name="TIME_RATE")
    private BigDecimal timeRate;


}
