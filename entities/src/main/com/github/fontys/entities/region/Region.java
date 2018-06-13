package com.github.fontys.entities.region;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.tracking.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity(name = "REGION")
@NamedQueries({
        @NamedQuery(
                name = Region.FIND_ALL,
                query = "SELECT r FROM REGION r ORDER BY r.name ASC"
        ),
        @NamedQuery(
                name = Region.FIND_ID,
                query = "SELECT r FROM REGION r WHERE r.id = :id"
        ),
        @NamedQuery(
                name = Region.FIND_NAME,
                query = "SELECT r FROM REGION r WHERE r.name = :name"
        )
})
public class Region implements Serializable{

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "Region.findAll";
    public static final String FIND_ID = "Region.findByID";
    public static final String FIND_NAME = "Region.findByName";


    //======================
    //==      Fields      ==
    //======================


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    // possible future extension; recursive regions (region(s) in region(s))

    @Column(name = "NAME")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "BORDER_POINTS",
            joinColumns = { @JoinColumn(name="REGION_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LOCATION_ID", referencedColumnName="ID")})
    private List<BorderLocation> borderPoints; // must be ordered consecutively

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "region")
    private List<Rate> rates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADDED_DATE")
    private Calendar addedDate;

    public Region(String name, List<BorderLocation> borderPoints, List<Rate> rates) {
        this.name = name;
        this.borderPoints = borderPoints;
        this.rates = rates;
    }

    public Region(String name, List<BorderLocation> borderPoints) {
        this.name = name;
        this.borderPoints = borderPoints;
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

    public Calendar getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Calendar addedDate) {
        this.addedDate = addedDate;
    }

    public boolean isWithinRegion(Location location){
        return isWithinRegion(location.getX(), location.getY());
    }


    /**
     * Kijkt of een locatie zich binnen een bepaalde regio bevind.
     * Gaat er vanuit dat de punten zich in een juiste volgorde bevinden, anders gaat het mis.
     *
     * @param locX x location
     * @param locY y location
     * @return if it's withing the current region.
     */
    public boolean isWithinRegion(double locX, double locY) {
        List<BorderLocation> regionBorders = getBorderPoints();

        if (regionBorders.size() <= 0) {
            return false;
        }

        double minX = regionBorders.get(0).getX();
        double maxX = regionBorders.get(0).getX();
        double minY = regionBorders.get(0).getY();
        double maxY = regionBorders.get(0).getY();

        for (BorderLocation r : regionBorders) {
            minX = Math.min(r.getX(), minX);
            maxX = Math.max(r.getX(), maxX);
            minY = Math.min(r.getY(), minY);
            maxY = Math.max(r.getY(), maxY);
        }

        //check if the location is outside of the boundries of all points.
        if (locX < minX
                || locX > maxX
                || locY < minY
                || locY > maxY) {
            return false;
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i = 0, j = regionBorders.size() - 1; i < regionBorders.size(); j = i++) {
            if ((regionBorders.get(i).getY() > locY) != (regionBorders.get(j).getY() > locY) &&
                    locX < (regionBorders.get(j).getX() - regionBorders.get(i).getX()) * (locY - regionBorders.get(i).getY()) / (regionBorders.get(j).getY() - regionBorders.get(i).getY()) + regionBorders.get(i).getX()) {
                inside = !inside;
            }
        }

        return inside;
    }
}
