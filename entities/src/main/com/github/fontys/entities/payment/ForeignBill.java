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

@Entity(name = "FOREIGN_BILL")
@NamedQueries({
        @NamedQuery(name = ForeignBill.FIND_ALL,
                query = "SELECT b FROM FOREIGN_BILL b"),

        @NamedQuery(name = ForeignBill.FIND_BYID,
                query = "SELECT b FROM FOREIGN_BILL b WHERE b.id=:id"),

        @NamedQuery(name = ForeignBill.FIND_BYVEHICLEID,
                query = "SELECT b FROM FOREIGN_BILL b WHERE b.registeredVehicle.id=:id"),

        @NamedQuery(name = ForeignBill.FIND_BYSTATUS,
                query = "SELECT b FROM FOREIGN_BILL b WHERE b.status=:status"),

        @NamedQuery(name = ForeignBill.FIND_BYOWNERID,
                query = "SELECT b FROM FOREIGN_BILL b WHERE b.registeredVehicle.customer.id=:ownerid"),
})
public class ForeignBill extends Bill {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_ALL = "ForeignBill.findAll";
    public static final String FIND_BYID = "ForeignBill.findByID";
    public static final String FIND_BYVEHICLEID = "ForeignBill.findByVehicleId";
    public static final String FIND_BYSTATUS = "ForeignBill.findByStatus";
    public static final String FIND_BYOWNERID = "ForeignBill.findByOwnerId";

    // ======================================
    // =             Fields              =
    // ======================================

    public ForeignBill() {

    }

    public ForeignBill(RegisteredVehicle registeredVehicle,
                       BigDecimal price,
                       BigDecimal alreadyPaid,
                       Calendar startDate,
                       Calendar endDate,
                       PaymentStatus paymentStatus,
                       double mileage) {
        super(registeredVehicle, price, alreadyPaid, startDate, endDate, paymentStatus, mileage, false);
    }
}
