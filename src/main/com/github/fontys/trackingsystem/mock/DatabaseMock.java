package com.github.fontys.trackingsystem.mock;


import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.CustomerVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.LocationDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.Currency;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.tracking.Hardware;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;
import org.glassfish.jersey.internal.inject.Custom;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

import static java.lang.System.in;

@ApplicationScoped
public class DatabaseMock {

    private static int AMOUNT_TO_GENERATE = 5;

    private List<TrackedVehicle> trackedVehicles = new ArrayList<>();
    private List<CustomerVehicle> customerVehicles = new ArrayList<>();
    private Map<String, Location> trackedLocations = new HashMap<String, Location>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();

    @Inject
    VehicleDAO vehicleDAO;

    @Inject
    TrackedVehicleDAO trackedVehicleDAO;

    @Inject
    CustomerVehicleDAO customerVehicleDAO;

    @Inject
    LocationDAO locationDAO;

    @PostConstruct
    public void init() {
        vehicles = generateDummyVehicles();
        customerVehicles = generateDummyCustomerVehicles(vehicles);
        bills = generateDummyBills(customerVehicles);

        // Insert into the real database

        for (Vehicle v : vehicles) {
            vehicleDAO.create(v);
        }
        for (CustomerVehicle cv : customerVehicles) {
            customerVehicleDAO.create(cv);
        }
        for (TrackedVehicle tv : trackedVehicles) {
            trackedVehicleDAO.create(tv);
        }

        Iterator it = trackedLocations.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Location entryLocation = (Location) entry.getValue();
            locationDAO.create(entryLocation);
        }
    }

    private List<Vehicle> generateDummyVehicles() {
        List<Vehicle> result = new ArrayList<>();
        Date date = new Date();
        result.add(new Vehicle("Dikke BMW", new VehicleModel(1L, "i8", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Dikke BMW", new VehicleModel(2L, "m4", "", FuelType.GASOLINE, EnergyLabel.B), date));
        result.add(new Vehicle("Audi", new VehicleModel(3L, "A4", "Sport", FuelType.DIESEL, EnergyLabel.D), date));
        result.add(new Vehicle("Porsche", new VehicleModel(4L, "911", "Turbo S", FuelType.DIESEL, EnergyLabel.E), date));
        result.add(new Vehicle("Koeningsegg", new VehicleModel(5L, "Agrerra", "R", FuelType.LPG, EnergyLabel.C), date));
        result.add(new Vehicle("Lamborghini", new VehicleModel(6L, "Aventador", "", FuelType.DIESEL, EnergyLabel.F), date));
        result.add(new Vehicle("Volkswagen", new VehicleModel(7L, "Polo", "GT", FuelType.DIESEL, EnergyLabel.D), date));
        result.add(new Vehicle("Opel", new VehicleModel(8L, "Ampera", "", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(9L, "Model S", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(10L, "Model 3", "65", FuelType.ELECTRIC, EnergyLabel.A), date));
        result.add(new Vehicle("Tesla", new VehicleModel(11L, "Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A), date));
        return result;
    }

    private List<CustomerVehicle> generateDummyCustomerVehicles(List<Vehicle> vehicles) {
        List<CustomerVehicle> result = new ArrayList<>();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            User c = new User(
                    String.format("Name %s", i),
                    String.format("Address %s", i),
                    String.format("Residency %s", i),
                    Role.BILL_ADMINISTRATOR);

            CustomerVehicle cv = new CustomerVehicle(
                    (long)i,
                    c,
                    String.format("XXX-00%s", i),
                    vehicles.get(i),
                    String.format("Proof %s", i));
            result.add(cv);
        }
        return result;
    }

    private List<Bill> generateDummyBills(List<CustomerVehicle> cvs) {
        List<Bill> dummyBills = new ArrayList<>();
        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {
            Calendar startdate = new GregorianCalendar();
            startdate.set(2018, i, 1);
            Calendar endDate = new GregorianCalendar();
            endDate.set(2018, i, 28);
            TrackedVehicle tv = new TrackedVehicle(cvs.get(i), new Location(50 + i / 4, 9 + i / 4, startdate), new Hardware((long)10, "Hwtype"));
            trackedVehicles.add(tv);
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

    public List<TrackedVehicle> getTrackedVehicles() {
        return trackedVehicles;
    }

    public Map<String, Location> getTrackedLocations() { return trackedLocations; }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Bill> getBills() {
        return bills;
    }
}
