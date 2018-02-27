package com.github.fontys;

import java.awt.geom.Point2D;
import java.util.List;

public class Region {
    // possible future extension; recursive regions (region(s) in region(s))
    private String name;
    private List<Point2D.Double> borderPoints; // must be ordered consecutively
    private List<Rate> rates;

    public Region(String name, List<Point2D.Double> borderPoints, List<Rate> rates) {
        this.name = name;
        this.borderPoints = borderPoints;
        this.rates = rates;
    }
}
