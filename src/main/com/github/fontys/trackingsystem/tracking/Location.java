package com.github.fontys.trackingsystem.tracking;

import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Date;

public class Location {
    private double lastLocationX;

    public double getLastLocationX() {
        return lastLocationX;
    }

    public void setLastLocationX(double lastLocationX) {
        this.lastLocationX = lastLocationX;
    }

    public double getLastLocationY() {
        return lastLocationY;
    }

    public void setLastLocationY(double lastLocationY) {
        this.lastLocationY = lastLocationY;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    private double lastLocationY;
    private Calendar time;

    public Location(double lastLocationX, double lastLocationY, Calendar time) {
        this.lastLocationX = lastLocationX;
        this.lastLocationY = lastLocationY;
        this.time = time;
    }
}
