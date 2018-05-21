package com.github.fontys.trackingsystem;

import com.github.fontys.trackingsystem.dao.interfaces.*;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.tracking.Hardware;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.transfer.Transfer;
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

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private TradeDAO tradeDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private BillDAO billDAO;

    private static int AMOUNT_TO_GENERATE = 10;

    private static int AMOUNT_LOCATIONS_TO_GENERATE = 20;
    @PostConstruct
    public void init() {

        Date date = new Date();
        User admin = new User("admin", "straat", "berlijn", Role.BILL_ADMINISTRATOR);
        Account adminAcc = new Account("admin@admin.com", "admin", "admin");
        admin.setVerified(true);
        adminAcc.setUser(admin);
        accountDAO.create(adminAcc);

        generateSimpleTransfer();

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
                    Role.CUSTOMER);
            u.setVerified(true);

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

            for (int x = 0; x < AMOUNT_LOCATIONS_TO_GENERATE; x++) {
                Calendar newstartdate = new GregorianCalendar();
                newstartdate.set(2018, i, x);
                Location l = new Location(50 + i + x / 4, 9 + i + x / 4, newstartdate);
                tv.setLastLocation(l);
            }

            Bill b = new Bill(
                    rv,
                    new BigDecimal(i * 200),
                    new BigDecimal(i * 400),
                    startdate,
                    endDate,
                    PaymentStatus.OPEN,
                    1000
            );

            em.persist(b);
            accountDAO.create(account);
            em.persist(tv);
            em.persist(u);
        }
    }

    private void generateSimpleTransfer() {
        Date date = new Date();
        User transferFrom = new User("transferFrom", "trans", "trans", Role.BILL_ADMINISTRATOR);
        Account transferFromAcc = new Account("transfer@from.com", "transferFrom", "transferFrom");
        transferFrom.setVerified(true);
        transferFromAcc.setUser(transferFrom);
        accountDAO.create(transferFromAcc);

        User transferTo = new User("transferTo", "trans", "trans", Role.BILL_ADMINISTRATOR);
        Account transferToAcc = new Account("transfer@to.com", "transferTo", "transferTo");
        transferTo.setVerified(true);
        transferToAcc.setUser(transferTo);
        accountDAO.create(transferToAcc);

        Vehicle trans = new Vehicle(
                String.format("Brand trans"),
                date,
                String.format("Model trans"),
                String.format("Edition trans"),
                FuelType.ELECTRIC,
                EnergyLabel.A);

        RegisteredVehicle rv = new RegisteredVehicle(transferFrom,
                String.format("XXX-FROM"),
                trans,
                String.format("Proof trans"));
        registeredVehicleDAO.create(rv);

        Transfer transfer = new Transfer(transferFrom, rv, "CXCXCXCXCXCX");
        tradeDAO.create(transfer);
    }

    public static int getAmountToGenerate() {
        return AMOUNT_TO_GENERATE;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void setVehicleDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setRegisteredVehicleDAO(RegisteredVehicleDAO registeredVehicleDAO) {
        this.registeredVehicleDAO = registeredVehicleDAO;
    }

    public void setBillDAO(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public void setTradeDAO(TradeDAO tradeDAO) {
        this.tradeDAO = tradeDAO;
    }
}
