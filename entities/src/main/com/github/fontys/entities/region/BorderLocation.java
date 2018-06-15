package com.github.fontys.entities.region;

import javax.persistence.*;

@Entity(name = "BORDER_LOCATION")
public class BorderLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VERTICES_ID")
    private Long verticeId;

    @Column(name="LAT")
    private double lat;

    @Column(name="LON")
    private double lon;

    public BorderLocation(){

    }

    public BorderLocation(double lat, double lon, Long verticeId){
        this.lat = lat;
        this.lon = lon;
        this.verticeId = verticeId;
    }

    public Long getVerticeId() {
        return verticeId;
    }

    public void setVerticeId(Long verticeId) {
        this.verticeId = verticeId;
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

    public Long getId() {
        return id;
    }
}
