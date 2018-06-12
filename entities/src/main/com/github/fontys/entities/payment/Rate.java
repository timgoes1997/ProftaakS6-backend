package com.github.fontys.entities.payment;

import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.EnergyLabel;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Entity(name = "RATE")
@NamedQueries({
        @NamedQuery(
                name = Rate.FIND_ALL,
                query = "SELECT r FROM RATE r ORDER BY r.addedDate ASC"
        ),
        @NamedQuery(
                name = Rate.FIND_ID,
                query = "SELECT r FROM RATE r WHERE r.id = :id"
        ),
        @NamedQuery(
                name = Rate.FIND_BY_REGION,
                query = "SELECT r FROM RATE r WHERE r.region.id = :id ORDER BY r.addedDate ASC"
        )
})
public class Rate {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "RegionRate.findAll";
    public static final String FIND_ID = "RegionRate.findByID";
    public static final String FIND_BY_REGION = "RegionRate.findByRegion";


    //======================
    //==      Fields      ==
    //======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 2)
    @Column(name = "KILOMETER_PRICE")
    private BigDecimal kilometerPrice;

    // possible extension; add employee who assigned this rate

    @Enumerated(EnumType.STRING)
    @Column(name = "ENERGYLABEL")
    private EnergyLabel energyLabel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Calendar startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Calendar endTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ADDED_DATE")
    private Calendar addedDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User authorizer;


    public Rate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime, Calendar addedDate, User authorizer) {
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.addedDate = addedDate;
        this.authorizer = authorizer;
    }

    public Rate() {
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BigDecimal getKilometerPrice() {
        return kilometerPrice;
    }

    public void setKilometerPrice(BigDecimal kilometerPrice) {
        this.kilometerPrice = kilometerPrice;
    }

    public EnergyLabel getEnergyLabel() {
        return energyLabel;
    }

    public void setEnergyLabel(EnergyLabel energyLabel) {
        this.energyLabel = energyLabel;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Calendar getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Calendar addedDate) {
        this.addedDate = addedDate;
    }

    public User getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(User authorizer) {
        this.authorizer = authorizer;
    }

    public Long getId() {
        return id;
    }
}
