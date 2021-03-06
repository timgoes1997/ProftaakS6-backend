package com.github.fontys.entities.payment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.fontys.entities.vehicle.RegisteredVehicle;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
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

        @NamedQuery(name = Bill.FIND_BYTYPE,
                query = "SELECT b FROM BILL b WHERE b.discriminator=0"),
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Bill implements Serializable {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "Bill.findAll";
    public static final String FIND_BYID = "Bill.findByID";
    public static final String FIND_BYVEHICLEID = "Bill.findByVehicleId";
    public static final String FIND_BYSTATUS = "Bill.findByStatus";
    public static final String FIND_BYOWNERID = "Bill.findByOwnerId";
    public static final String FIND_BYTYPE = "Bill.findByType";

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "TYPE")
    protected int discriminator;

    @OneToMany
    private List<Bill> montlyBills;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "BILL_ROUTES",
            joinColumns = { @JoinColumn(name="BILL_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="ROUTE_ID", referencedColumnName="ID")})
    private List<Route> billRoutes; // must be ordered consecutively

    public Bill() {
        this.discriminator = 0;
    }

    public Bill(RegisteredVehicle registeredVehicle,
                BigDecimal price,
                BigDecimal alreadyPaid,
                Calendar startDate,
                Calendar endDate,
                PaymentStatus paymentStatus,
                double mileage,
                boolean isEndOfMonthBill,
                List<Route> routes) {
        this.registeredVehicle = registeredVehicle;
        this.price = price;
        this.alreadyPaid = alreadyPaid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = paymentStatus;
        this.mileage = mileage;
        this.isEndOfMonthBill = isEndOfMonthBill;
        this.billRoutes = routes;
        this.discriminator = 0;
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
        this.discriminator = 0;
    }

    @JsonIgnore
    @XmlTransient
    @JsonbTransient
    public RegisteredVehicle getRegisteredVehicle() {
        return registeredVehicle;
    }

    public void setRegisteredVehicle(RegisteredVehicle registeredVehicle) {
        this.registeredVehicle = registeredVehicle;
    }

    @JsonGetter
    @JsonbProperty
    @XmlAttribute
    public long getNumberStartDate() {
        return startDate.getTimeInMillis() / 1000;
    }

    @JsonGetter
    @JsonbProperty
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

    public int getDiscriminator() { return discriminator; }

    public void setDiscriminator(int discriminator) {
        this.discriminator = discriminator;
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

    @JsonGetter
    @JsonbProperty
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

    @JsonGetter
    @JsonbProperty
    @XmlAttribute
    public String getMonth() {
        return new SimpleDateFormat("MMM").format(startDate.getTime());
    }

    @JsonGetter
    @JsonbProperty
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

    public List<Route> getBillRoutes() {
        return billRoutes;
    }

    public void setBillRoutes(List<Route> billRoutes) {
        this.billRoutes = billRoutes;
    }
}
