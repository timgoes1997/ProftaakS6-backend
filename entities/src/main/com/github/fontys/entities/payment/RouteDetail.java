package com.github.fontys.entities.payment;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Entity(name = "ROUTE_DETAIL")
public class RouteDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Calendar startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Calendar endTime;

    @Column(name = "DISTANCE")
    private double distance;

    @Digits(integer = 12, fraction = 4)
    @Column(name = "PRICE", precision = 16, scale = 4)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RATE_ID")
    private Rate rate;

    public RouteDetail(Calendar startTime, Calendar endTime, double distance, BigDecimal price, Rate rate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.price = price;
        this.rate = rate;
    }

    public RouteDetail(Calendar startTime, Calendar endTime, double distance, BigDecimal price){
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.price = price;
    }

    public RouteDetail() {
    }

    public Long getId() {
        return id;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
}
