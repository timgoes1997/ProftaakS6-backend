package com.github.fontys.trackingsystem.region;

import javax.persistence.*;

@Entity(name = "BORDER_LOCATION")
public class BorderLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="X")
    private double x;

    @Column(name="Y")
    private double y;

    public BorderLocation(){

    }

    public BorderLocation(double x, double y){
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
