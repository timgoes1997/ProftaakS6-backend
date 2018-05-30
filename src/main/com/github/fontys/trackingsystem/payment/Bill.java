package com.github.fontys.trackingsystem.payment;

import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity(name = "BILL")
@NamedQueries({
        @NamedQuery(name = Bill.FIND_ALL,
                query = "SELECT b FROM BILL b"),

        @NamedQuery(name = Bill.FIND_BYID,
                query = "SELECT b FROM BILL b WHERE b.id=:id"),

        @NamedQuery(name = Bill.FIND_BYVEHICLEID,
                query = "SELECT b FROM BILL b WHERE b.registeredVehicle.id=:id"),
        // TODO: 2-4-18 Idk of je zo bij een nested class kan met jpa queries

        @NamedQuery(name = Bill.FIND_BYSTATUS,
                query = "SELECT b FROM BILL b WHERE b.status=:status"),

        @NamedQuery(name = Bill.FIND_BYOWNERID,
                query = "SELECT b FROM BILL b WHERE b.registeredVehicle.customer.id=:ownerid"),
})
public class Bill implements Serializable {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "Bill.findAll";
    public static final String FIND_BYID = "Bill.findByID";
    public static final String FIND_BYVEHICLEID = "Bill.findByVehicleId";
    public static final String FIND_BYSTATUS = "Bill.findByStatus";
    public static final String FIND_BYOWNERID = "Bill.findByOwnerId";

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CUSTOMER_VEHICLE_ID")
    private RegisteredVehicle registeredVehicle;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "ALREADY_PAID")
    private BigDecimal alreadyPaid;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Calendar startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Calendar endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS")
    private PaymentStatus status;

    @Column(name = "MILEAGE")
    private double mileage;

    @Column(name = "ISENDOFMONTHBILL")
    private boolean isEndOfMonthBill;

    @OneToMany
    private List<Bill> montlyBills;

    public Bill() {

    }

    public Bill(RegisteredVehicle registeredVehicle,
                BigDecimal price,
                BigDecimal alreadyPaid,
                Calendar startDate,
                Calendar endDate,
                PaymentStatus paymentStatus,
                double mileage,
                boolean isEndOfMonthBill) {
        this.registeredVehicle = registeredVehicle;
        this.price = price;
        this.alreadyPaid = alreadyPaid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = paymentStatus;
        this.mileage = mileage;
        this.isEndOfMonthBill = isEndOfMonthBill;
    }

    @XmlTransient
    public RegisteredVehicle getRegisteredVehicle() {
        return registeredVehicle;
    }

    public void setRegisteredVehicle(RegisteredVehicle registeredVehicle) {
        this.registeredVehicle = registeredVehicle;
    }

    @XmlAttribute
    public long getNumberStartDate() {
        return startDate.getTimeInMillis() / 1000;
    }

    @XmlAttribute
    public long getNumberEndDate() {
        return endDate.getTimeInMillis() / 1000;
    }

    public Calendar getCalendarStartDate() {
        return startDate;
    }


    public Calendar getCalendarEndDate() {
        return endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(BigDecimal alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }


    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @XmlAttribute
    public String getLicense() {
        return registeredVehicle.getLicensePlate();
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    @XmlAttribute
    public String getMonth() {
        return new SimpleDateFormat("MMM").format(startDate.getTime());
    }

    @XmlAttribute
    public Long getBillnr() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public boolean isEndOfMonthBill() {
        return isEndOfMonthBill;
    }

    public void setEndOfMonthBill(boolean endOfMonthBill) {
        isEndOfMonthBill = endOfMonthBill;
    }

    public void setMontlyBills(List<Bill> bills) {
        this.montlyBills = bills;
    }

    public void addMonthlyBill(Bill bill) {
        this.montlyBills.add(bill);
    }

    public List<Bill> getMontlyBills() {
        return this.montlyBills;
    }
}
