package com.github.fontys.entities.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.EnergyLabel;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.ws.rs.NotAcceptableException;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        ),
        @NamedQuery(
                name = Rate.FIND_DEFAULT,
                query = "SELECT r FROM RATE r WHERE r.region IS NULL"
        )
})
public class Rate implements Serializable {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "RegionRate.findAll";
    public static final String FIND_ID = "RegionRate.findByID";
    public static final String FIND_BY_REGION = "RegionRate.findByRegion";
    public static final String FIND_DEFAULT = "RegionRate.findDefaultRate";

    //======================
    //==      Fields      ==
    //======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 4)
    @Column(name = "KILOMETER_PRICE", precision = 16, scale = 4)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User authorizer;


    public Rate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, int startDay, int startHour, int startMinute, int endDay, int endHour, int endMinute, User authorizer){
        if(startDay <= 0 || startDay > 7 || startDay > endDay || endDay > 7 || endDay < startDay){
            throw new NotAcceptableException("Given day is limited from 1-7 make use of Calender.DAY_OF_WEEK, see Calender.SUNDAY etc. Startday also can't be greater than endDay");
        }

        if(startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23){
            throw new NotAcceptableException("Hours need a value of 0-23");
        }

        if(startMinute < 0 || startMinute > 59 || endMinute < 0 || endMinute > 59){
            throw new NotAcceptableException("Minutes need a value between 0-59");
        }

        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = getTime(startDay, startHour, startMinute);
        this.endTime = getTime(endDay, endHour, endMinute);
        this.addedDate = Calendar.getInstance();
        this.authorizer = authorizer;

        if(startTime.after(endTime)){
            throw new NotAcceptableException("Startime needs to be before endtime!");
        }
    }

    public Rate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime, User authorizer) {
        if(endTime.before(startTime)){
            throw new NotAcceptableException("Endtime needs to be after the starttime");
        }
        if(endTime.get(Calendar.DAY_OF_WEEK) < startTime.get(Calendar.DAY_OF_WEEK) || startTime.get(Calendar.WEEK_OF_YEAR) != endTime.get(Calendar.WEEK_OF_YEAR)){
            throw new NotAcceptableException("Setting up a rate is limited to a week, per day is preferred!");
        }
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.addedDate = Calendar.getInstance();
        this.authorizer = authorizer;
    }

    public Rate() {
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
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

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    public User getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(User authorizer) {
        this.authorizer = authorizer;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    public boolean isInRate(EnergyLabel energyLabel) {
        //check labels
        if (energyLabel != this.energyLabel) return false;

        //check day
        Calendar currentTime = GregorianCalendar.getInstance();
        currentTime = getTime(currentTime.get(Calendar.DAY_OF_WEEK), currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE));
        Calendar start = getTime(startTime.get(Calendar.DAY_OF_WEEK), startTime.get(Calendar.HOUR_OF_DAY),startTime.get(Calendar.MINUTE));
        Calendar end = getTime(endTime.get(Calendar.DAY_OF_WEEK), endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));

        return start.before(currentTime) && end.after(currentTime);
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    private Calendar getTime(int currentDay, int currentHour, int currentMinute) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, currentDay, currentHour, currentMinute, 0);
        return calendar;
    }
}
