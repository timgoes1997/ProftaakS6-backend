package com.github.fontys.entities.region;

import com.github.fontys.entities.payment.Rate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "REGION")
public class Region implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // possible future extension; recursive regions (region(s) in region(s))

    @Column(name = "NAME")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BORDER_POINTS",
            joinColumns = { @JoinColumn(name="REGION_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")})
    private List<BorderLocation> borderPoints; // must be ordered consecutively

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    private List<Rate> rates;

    public Region(String name, List<BorderLocation> borderPoints, List<Rate> rates) {
        this.name = name;
        this.borderPoints = borderPoints;
        this.rates = rates;
    }

    public Region() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BorderLocation> getBorderPoints() {
        return borderPoints;
    }

    public void setBorderPoints(List<BorderLocation> borderPoints) {
        this.borderPoints = borderPoints;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public Long getId() {
        return id;
    }
}
