package com.github.fontys.trackingsystem.tracking;

import java.util.Calendar;

public class Location {
    private double x;
    private double y;

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

    private Calendar time;

    /*
        For reflective code only
     */
    @Deprecated
    public Location() {}

    public Location(double lastLocationX, double lastLocationY, Calendar time) {
        this.x = lastLocationX;
        this.y = lastLocationY;
        this.time = time;
    }
}
