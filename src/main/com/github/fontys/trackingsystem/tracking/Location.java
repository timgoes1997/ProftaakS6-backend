package com.github.fontys.trackingsystem.tracking;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Date;

public class Location implements Serializable {
    private double lastLocationX;
    private double lastLocationY;
    private Date time;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Location(double lastLocationX, double lastLocationY, Date time) {

        this.lastLocationX = lastLocationX;
        this.lastLocationY = lastLocationY;
        this.time = time;
    }
}
