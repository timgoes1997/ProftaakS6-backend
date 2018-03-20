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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DatabaseMock {

    private static int AMOUNT_TO_GENERATE = 5;

    private List<CustomerVehicle> customerVehicles = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    @PostConstruct
    public void init() {
        vehicles = generateDummyVehicles();
        customerVehicles = generateDummyCustomerVehicles(vehicles);
        bills = generateDummyBills(customerVehicles);
    }

    private List<Vehicle> generateDummyVehicles() {
        List<Vehicle> result = new ArrayList<>();
        Date date = new Date();
        result.add(new Vehicle("Dikke BMW", new VehicleModel(1, "i8", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Dikke BMW", new VehicleModel(2, "m4", "", FuelType.GASOLINE, EnergyLabel.B), date));
        result.add(new Vehicle("Audi", new VehicleModel(3, "A4", "Sport", FuelType.DIESEL, EnergyLabel.D), date));
        result.add(new Vehicle("Porsche", new VehicleModel(4, "911", "Turbo S", FuelType.DIESEL, EnergyLabel.E), date));
        result.add(new Vehicle("Koeningsegg", new VehicleModel(5, "Agrerra", "R", FuelType.LPG, EnergyLabel.C), date));
        result.add(new Vehicle("Lamborghini", new VehicleModel(6, "Aventador", "", FuelType.DIESEL, EnergyLabel.F), date));
        result.add(new Vehicle("Volkswagen", new VehicleModel(7, "Polo", "GT", FuelType.DIESEL, EnergyLabel.D), date));
        result.add(new Vehicle("Opel", new VehicleModel(8, "Ampera", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(9, "Model S", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(10, "Model 3", "65", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(11, "Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
        return result;
    }

    private List<CustomerVehicle> generateDummyCustomerVehicles(List<Vehicle> vehicles) {
        List<CustomerVehicle> result = new ArrayList<>();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            Customer c = new Customer(
                    String.format("Name %s", i),
                    String.format("Address %s", i),
                    String.format("Residency %s", i),
                    Role.BILL_ADMINISTRATOR);

            CustomerVehicle cv = new CustomerVehicle(
                    i,
                    c,
                    String.format("Licenseplate %s", i),
                    vehicles.get(i),
                    String.format("Proof %s", i));
            result.add(cv);
        }
        return result;
    }

    private List<Bill> generateDummyBills(List<CustomerVehicle> cvs) {
        List<Bill> dummyBills = new ArrayList<>();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
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
                    PaymentStatus.OPEN,
                    1000
            );
            dummyBills.add(b);
        }
        return dummyBills;
    }

    public List<String> getBrands() {
        List<String> brands = new ArrayList<>();
        for (Vehicle v : vehicles) {
            boolean brandAlreadyExists = brands.contains(v.getBrand());
            if (!brandAlreadyExists) {
                brands.add(v.getBrand());
            }
        }
        return brands;
    }

    public List<Vehicle> getVehiclesByBrand(String brand) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v.getBrand().equals(brand)) {
                result.add(v);
            }
        }
        return result;
    }

    public Vehicle newVehicle(String brand, int modelID, Date buildDate) {
        Vehicle vehicle = null;
        VehicleModel vm = null;
        for (Vehicle v : vehicles) {
            if (v.getVehicleModel().getId() == modelID) {
                vm = v.getVehicleModel();
            }
            if (v.getBrand().equals(brand) && modelID == v.getVehicleModel().getId()) {
                vehicle = v;
            }
        }

        if (vehicle != null) return vehicle;
        if (vm != null) {
            Vehicle v = new Vehicle(brand, vm, buildDate);
            vehicles.add(v);
            return v;
        }
        return null;
    }

    public static int getAmountToGenerate() {
        return AMOUNT_TO_GENERATE;
    }

    public List<CustomerVehicle> getCustomerVehicles() {
        return customerVehicles;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Bill> getBills() {
        return bills;
    }
}
