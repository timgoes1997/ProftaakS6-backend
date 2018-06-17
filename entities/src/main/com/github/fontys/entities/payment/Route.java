package com.github.fontys.entities.payment;


import com.github.fontys.entities.tracking.Location;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Entity(name = "ROUTE")
public class Route {

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ROUTE_LOCATIONS",
            joinColumns = { @JoinColumn(name="ROUTE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")})
    private List<Location> locations; // must be ordered consecutively

    @Column(name = "TOTAL_DISTANCE")
    private double distance;

    @Column(name = "TOTAL_PRICE")
    private BigDecimal price;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "ROUTE_ROUTE_DETAILS",
            joinColumns = { @JoinColumn(name="ROUTE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="DETAIL_ID", referencedColumnName="ID")})
    private List<RouteDetail> routeDetails; // must be ordered consecutively

    public Route(Calendar startTime, Calendar endTime, List<Location> locations, double distance, BigDecimal price) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.locations = locations;
        this.distance = distance;
        this.price = price;
    }

    public Route() {
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
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

    public List<RouteDetail> getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(List<RouteDetail> routeDetails) {
        this.routeDetails = routeDetails;
    }
}
