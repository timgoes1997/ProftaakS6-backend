package com.github.fontys.entities.payment;

import com.github.fontys.entities.vehicle.RegisteredVehicle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity()
@DiscriminatorValue("1")
@NamedQueries({
        @NamedQuery(name = ForeignBill.FIND_BYTYPE,
                query = "SELECT b FROM ForeignBill b WHERE b.discriminator=1"),
})
public class ForeignBill extends Bill {

    @Column(name = "COUNTRY")
    private String country;

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_BYTYPE = "ForeignBill.findByType";

    public ForeignBill() {
        this.discriminator = 1;
    }

    public ForeignBill(RegisteredVehicle registeredVehicle, BigDecimal price, BigDecimal alreadyPaid, Calendar startDate, Calendar endDate, PaymentStatus paymentStatus, double mileage, boolean isEndOfMonthBill, String country) {
        super(registeredVehicle, price, alreadyPaid, startDate, endDate, paymentStatus, mileage, isEndOfMonthBill);
        this.country = country;
        this.discriminator = 1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
