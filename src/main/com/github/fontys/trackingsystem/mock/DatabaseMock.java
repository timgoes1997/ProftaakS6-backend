package com.github.fontys.trackingsystem.mock;


import com.github.fontys.trackingsystem.EnergyLabel;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DatabaseMock {

    private List<CustomerVehicle> vehicles = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("Hello");
        for (int i = 0; i < 3; i++) {

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

            vehicles.add(cv);
            System.out.println(String.format("Added cv: %s", cv));
        }
    }

    public void setVehicles(List<CustomerVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<CustomerVehicle> getVehicles() {
        return vehicles;
    }
}
