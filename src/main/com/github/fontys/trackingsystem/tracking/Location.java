package com.github.fontys.trackingsystem.tracking;

import java.awt.geom.Point2D;
import java.util.Date;

public class Location {
    private Point2D.Double lastLocation;
    private Date time;

    public Location(Point2D.Double lastLocation, Date time) {
        this.lastLocation = lastLocation;
        this.time = time;
    }

    public Point2D.Double getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Point2D.Double lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
