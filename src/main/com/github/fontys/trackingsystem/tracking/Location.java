package com.github.fontys.trackingsystem.tracking;

import javax.persistence.*;
import java.util.Calendar;

@Entity(name = "TRACKING_LOCATION")
@NamedQueries({
// Add vehicleID as foreign key to Location...
       @NamedQuery(name = "Location.findByVehicleID",
               query = "SELECT TRACKING_LOCATION FROM TRACKED_VEHICLE a " +
                       "WHERE a.customerVehicle.id=:vehicleID")
})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="X")
    private double x;

    @Column(name="Y")
    private double y;

    @Temporal(TemporalType.DATE)
    @Column(name = "TIME")
    private Calendar time;

    public Location() {}

    public Location(double lastLocationX, double lastLocationY, Calendar time) {
        this.x = lastLocationX;
        this.y = lastLocationY;
        this.time = time;
    }

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

    public Long getId() {
        return id;
    }
}
