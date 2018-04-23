package com.github.fontys.trackingsystem;


import com.github.fontys.trackingsystem.dao.interfaces.*;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.Currency;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.tracking.Hardware;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;

@Singleton
@Startup
public class DummyDataGenerator {

    @PersistenceContext(name = "Proftaak")
    private EntityManager em;

    private static int AMOUNT_TO_GENERATE = 15;

    @PostConstruct
    public void init() {

        Date date = new Date();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            Vehicle v = new Vehicle(
                    String.format("Brand '%s", i),
                    date,
                    String.format("Model '%s", i),
                    String.format("Edition '%s'", i),
                    FuelType.ELECTRIC,
                    EnergyLabel.A);
            User u = new User(
                    String.format("Name %s", i),
                    String.format("Address %s", i),
                    String.format("Residency %s", i),
                    Role.BILL_ADMINISTRATOR);

            Account account = new Account(String.format("email %s", i), String.format("user %s", i), String.format("password %s", i));
            account.setUser(u);

            RegisteredVehicle rv = new RegisteredVehicle(
                    (long) i,
                    u,
                    String.format("XXX-00%s", i),
                    v,
                    String.format("Proof %s", i));

            Calendar startdate = new GregorianCalendar();
            startdate.set(2018, i, 1);
            Calendar endDate = new GregorianCalendar();
            endDate.set(2018, i, 28);
            TrackedVehicle tv = new TrackedVehicle(rv, new Location(50 + i / 4, 9 + i / 4, startdate), new Hardware((long) 10, "Hwtype"));
            Bill b = new Bill(
                    rv,
                    Currency.EUR,
                    new BigDecimal(i * 200),
                    new BigDecimal(i * 400),
                    startdate,
                    endDate,
                    PaymentStatus.OPEN,
                    1000
            );

            em.persist(b);
            em.persist(account);
            em.persist(tv);
            em.persist(u);
        }
    }
}
