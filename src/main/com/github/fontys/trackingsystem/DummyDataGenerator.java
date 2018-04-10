package com.github.fontys.trackingsystem;


import com.github.fontys.trackingsystem.EnergyLabel;
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
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;

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

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private CustomerVehicleDAO customerVehicleDAO;

    @Inject
    private BillDAO billDAO;

    private static int AMOUNT_TO_GENERATE = 5;

    @PostConstruct
    public void init() {
        generateDummyVehicles();
        generateDummyCustomerVehicles();
        generateDummyBills();
    }

    public void updateBillStatus(Bill b, String newStatus) {
        // TODO: 31-3-18 actually update bill status in the real database
    }

    private void generateDummyVehicles() {
        Date date = new Date();
        vehicleDAO.create(new Vehicle("Dikke BMW", new VehicleModel(1L, "i8", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        vehicleDAO.create(new Vehicle("Dikke BMW", new VehicleModel(2L, "m4", "", FuelType.GASOLINE, EnergyLabel.B), date));
        vehicleDAO.create(new Vehicle("Audi", new VehicleModel(3L, "A4", "Sport", FuelType.DIESEL, EnergyLabel.D), date));
        vehicleDAO.create(new Vehicle("Porsche", new VehicleModel(4L, "911", "Turbo S", FuelType.DIESEL, EnergyLabel.E), date));
        vehicleDAO.create(new Vehicle("Koeningsegg", new VehicleModel(5L, "Agrerra", "R", FuelType.LPG, EnergyLabel.C), date));
        vehicleDAO.create(new Vehicle("Lamborghini", new VehicleModel(6L, "Aventador", "", FuelType.DIESEL, EnergyLabel.F), date));
        vehicleDAO.create(new Vehicle("Volkswagen", new VehicleModel(7L, "Polo", "GT", FuelType.DIESEL, EnergyLabel.D), date));
        vehicleDAO.create(new Vehicle("Opel", new VehicleModel(8L, "Ampera", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        vehicleDAO.create(new Vehicle("Tesla", new VehicleModel(9L, "Model S", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
        vehicleDAO.create(new Vehicle("Tesla", new VehicleModel(10L, "Model 3", "65", FuelType.ELECTRIC, EnergyLabel.A), date));
        vehicleDAO.create(new Vehicle("Tesla", new VehicleModel(11L, "Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
    }

    private void generateDummyCustomerVehicles() {
        List<Vehicle> vehicles = vehicleDAO.findAll();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            User u = new User( //TODO: Account er bij aanmaken.
                    String.format("Name %s", i),
                    String.format("Address %s", i),
                    String.format("Residency %s", i),
                    Role.BILL_ADMINISTRATOR);

            Account account = new Account(String.format("email", i), String.format("user", i), String.format("password", i));
            account.setUser(u);
            accountDAO.create(account);

            User inDatabase = userDAO.findByAccount(accountDAO.findByEmail(account.getEmail()));

            CustomerVehicle cv = new CustomerVehicle(
                    (long)i,
                    inDatabase,
                    String.format("XXX-00%s", i),
                    vehicles.get(i),
                    String.format("Proof %s", i));

            customerVehicleDAO.create(cv);

        }
    }

    private void generateDummyBills() {
        List<CustomerVehicle> customerVehicles = customerVehicleDAO.getAll();

        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            Calendar startdate = new GregorianCalendar();
            startdate.set(2018, i, 1);
            Calendar endDate = new GregorianCalendar();
            endDate.set(2018, i, 28);
            TrackedVehicle tv = new TrackedVehicle(customerVehicles.get(i), new Location(50 + i / 4, 9 + i / 4, startdate), new Hardware((long)10, "Hwtype"));
            em.persist(tv);
            Bill b = new Bill(
                    customerVehicles.get(i),
                    Currency.EUR,
                    new BigDecimal(i * 200),
                    new BigDecimal(i * 400),
                    startdate,
                    endDate,
                    PaymentStatus.OPEN,
                    1000
            );
            billDAO.create(b);
        }
    }
}
