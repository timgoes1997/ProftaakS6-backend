package com.github.fontys.tracking;

import java.awt.geom.Point2D;
import java.util.Date;

public class Location {
    private Point2D.Double lastLocation;
    private Date time;

    public Location(Point2D.Double lastLocation, Date time) {
        this.lastLocation = lastLocation;
        this.time = time;
    }
}
