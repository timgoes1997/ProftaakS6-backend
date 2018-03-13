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

    private List<CustomerVehicle> cVehicles = new ArrayList<>();
    private List<Vehicle> veh = new ArrayList<>();

    private List<CustomerVehicle> vehicles = new ArrayList<>();
    private List<Bill> bills = new ArrayList<>();


    @PostConstruct
    public void init() {
        System.out.println("Hello");
        int lastIndex = 3;
        for (int i = 0; i < lastIndex; i++) {
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

            VehicleModel m = new VehicleModel(i,
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

            veh.add(v);
            cVehicles.add(cv);
            System.out.println(String.format("Added cv: %s", cv));
        }
        generateVehicles(lastIndex);

            result.add(cv);
            System.out.println(String.format("Added cv: %s", cv));
        }
        return result;
    }

    private List<Bill> generateDummyBills(List<CustomerVehicle> cvs) {
        List<Bill> dummyBills = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int oneday = 24 * 60 * 60 * 1000;
            Date startdate = new Date();

            startdate = new Date(startdate.getTime() - i * oneday);
            Date endDate = new Date(startdate.getTime() + i * 2 * oneday);

            TrackedVehicle tv = new TrackedVehicle(cvs.get(i), new Location(100, 200, startdate), new Hardware(10, "Hwtype"));

            Bill b = new Bill(
                    cvs.get(i),
                    Currency.EUR,
                    new BigDecimal(i * 2),
                    new BigDecimal(1 * 1.2),
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

    public void setCustomerVehicles(List<CustomerVehicle> vehicles) {
        this.cVehicles = vehicles;
    }

    public List<CustomerVehicle> getCustomerVehicles() {
        return cVehicles;
    }

    public List<Vehicle> getVehicles() {
        return veh;
    }

    public void setcVehicles(List<Vehicle> veh) {
        this.veh = veh;
    }

    public List<String> getBrands(){
        List<String> brands = new ArrayList<>();
        for (Vehicle v : veh ) {
            if(!brands.contains(v.getBrand())) brands.add(v.getBrand());
        }
        return brands;
    }

    public List<Vehicle> getVehicles(String brand){
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle v: veh) {
            if(v.getBrand().equals(brand)){
                vehicles.add(v);
            }
        }
        return vehicles;
    }

    public Vehicle newVehicle(String brand, int modelID, Date buildDate){
        Vehicle vehicle = null;
        VehicleModel vm = null;
        for (Vehicle v: veh) {
            if(v.getVehicleModel().getId() == modelID) {
                vm = v.getVehicleModel();
            }
            if(v.getBrand().equals(brand) && modelID == v.getVehicleModel().getId()){
                vehicle = v;
            }
        }

        if(vehicle != null) return vehicle;
        if(vm != null) {
            Vehicle v = new Vehicle(brand, vm, buildDate);
            veh.add(v);
            return v;
        }
        return null;
    }

    public void generateVehicles(int lastindex){
        veh.add(new Vehicle(String.format("Dikke BMW"), new VehicleModel(lastindex + 1, String.format("i8"), String.format(""), FuelType.ELECTRIC, EnergyLabel.A), new Date()));
        veh.add(new Vehicle(String.format("Dikke BMW"), new VehicleModel(lastindex + 2, String.format("m4"), String.format(""), FuelType.GASOLINE, EnergyLabel.B), new Date()));
        veh.add(new Vehicle(String.format("Audi"), new VehicleModel(lastindex + 3,String.format("A4"), String.format("Sport"), FuelType.DIESEL, EnergyLabel.D), new Date()));
        veh.add(new Vehicle(String.format("Porsche"), new VehicleModel(lastindex + 4,String.format("911"), String.format("Turbo S"), FuelType.DIESEL, EnergyLabel.E), new Date()));
        veh.add(new Vehicle(String.format("Koeningsegg"), new VehicleModel(lastindex + 5,String.format("Agrerra"), String.format("R"), FuelType.LPG, EnergyLabel.C), new Date()));
        veh.add(new Vehicle(String.format("Lamborghini"), new VehicleModel(lastindex + 6,String.format("Aventador"), String.format(""), FuelType.DIESEL, EnergyLabel.F), new Date()));
        veh.add(new Vehicle(String.format("Volkswagen"), new VehicleModel(lastindex + 7,String.format("Polo"), String.format("GT"), FuelType.DIESEL, EnergyLabel.D), new Date()));
        veh.add(new Vehicle(String.format("Opel"), new VehicleModel(lastindex + 8,String.format("Ampera"), String.format(""), FuelType.ELECTRIC, EnergyLabel.A), new Date()));
        veh.add(new Vehicle(String.format("Tesla"), new VehicleModel(lastindex + 9,String.format("Model S"), String.format("P100D"), FuelType.ELECTRIC, EnergyLabel.A), new Date()));
        veh.add(new Vehicle(String.format("Tesla"), new VehicleModel(lastindex + 10,String.format("Model 3"), String.format("65"), FuelType.ELECTRIC, EnergyLabel.A), new Date()));
        veh.add(new Vehicle(String.format("Tesla"), new VehicleModel(lastindex + 11,String.format("Model X"), String.format("P100D"), FuelType.ELECTRIC, EnergyLabel.A), new Date()));
    }
}
