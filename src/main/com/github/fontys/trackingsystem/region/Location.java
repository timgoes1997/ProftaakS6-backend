package com.github.fontys.trackingsystem.region;

import javax.persistence.*;

@Entity(name = "LOCATION")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="X")
    private double x;

    @Column(name="Y")
    private double y;

    public Location(){

    }

    public Location(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Long getId() {
        return id;
    }
}
