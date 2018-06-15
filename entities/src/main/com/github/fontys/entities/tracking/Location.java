package com.github.fontys.entities.tracking;

import javax.persistence.*;
import java.util.Calendar;

@Entity(name = "TRACKING_LOCATION")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name="LAT")
    private double lat;

    @Column(name="LON")
    private double lon;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME")
    private Calendar time;

    public Location() {}

    public Location(double lastLat, double lastLon, Calendar time) {
        this.lat = lastLat;
        this.lon = lastLon;
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }
}
