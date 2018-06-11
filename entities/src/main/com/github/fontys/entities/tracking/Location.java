package com.github.fontys.entities.tracking;

import javax.persistence.*;
import java.util.Calendar;

@Entity(name = "TRACKING_LOCATION")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name="X")
    private double x;

    @Column(name="Y")
    private double y;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIME")
    private Calendar time;

    public Location() {}

    public Location(double lastLocationX, double lastLocationY, Calendar time) {
        this.x = lastLocationX;
        this.y = lastLocationY;
        this.time = time;
    }

    public double getX() {
        return x;
    }

    public void setX(double lastLocationX) {
        this.x = lastLocationX;
    }

    public double getY() {
        return y;
    }

    public void setY(double lastLocationY) {
        this.y = lastLocationY;
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
