package com.github.fontys.trackingsystem.mock;


import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.Currency;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.tracking.Hardware;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Customer;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;
import sun.reflect.generics.scope.DummyScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DatabaseMock {

    private List<CustomerVehicle> vehicles = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    @PostConstruct
    public void init() {
        vehicles = generateDummyCustomerVehicles();
        bills = generateDummyBills(vehicles);
    }

    public List<Bill> getBills() {
        return bills;
    }

    private List<CustomerVehicle> generateDummyCustomerVehicles() {
        List<CustomerVehicle> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Customer c = new Customer(
                    String.format("Name %s", i),
                    String.format("Address %s", i),
                    String.format("Residency %s", i),
                    Role.BILL_ADMINISTRATOR);

            VehicleModel m = new VehicleModel(
                    String.format("Modelname %s", i),
                    String.format("Edition %s", i),
                    FuelType.DIESEL,
                    EnergyLabel.A);

            Date d = new Date();

            Vehicle v = new Vehicle(String.format("Brand %s", i), m, d);

            CustomerVehicle cv = new CustomerVehicle(
                    i,
                    c,
                    String.format("Licenseplate %s", i),
                    v,
                    String.format("Proof %s", i));

            result.add(cv);
        }
        return result;
    }

    private List<Bill> generateDummyBills(List<CustomerVehicle> cvs) {
        List<Bill> dummyBills = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int oneday = 24 * 60 * 60 * 1000;
            Date startdate = new Date();
             startdate = new Date(startdate.getTime() + i + (oneday * 4 * i));
            Date endDate = new Date(startdate.getTime() + i + (oneday * 8 * i));

            TrackedVehicle tv = new TrackedVehicle(cvs.get(i), new Location(100, 200, startdate), new Hardware(10, "Hwtype"));

            Bill b = new Bill(
                    cvs.get(i),
                    Currency.EUR,
                    new BigDecimal(i * 200),
                    new BigDecimal(i * 400),
                    startdate,
                    endDate,
                    tv,
                    PaymentStatus.OPEN,
                    1000
            );
            dummyBills.add(b);
        }
        return dummyBills;
    }

    public void setVehicles(List<CustomerVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<CustomerVehicle> getVehicles() {
        return vehicles;
    }
}
