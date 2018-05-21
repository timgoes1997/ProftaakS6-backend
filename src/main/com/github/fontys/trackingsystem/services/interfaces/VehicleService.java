package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import java.io.InputStream;
import java.util.List;

public interface VehicleService {
    List<RegisteredVehicle> getVehiclesFromUser();
    Vehicle getVehicle(int id);
    List<String> getBrands();
    List<Vehicle> getVehiclesByBrand(String brand);
    List<String> getVehicleModelsByBrand(String brand);
    List<String> getEditionsByModelAndBrand(String brand, String model);
    List<String> getFuelTypesByModelBrandAndEdition(String brand, String model, String edition);
    List<String> getEnergyLabelsByModelBrandAndEdition(String brand, String model, String edition);
    List<String> getEnergyLabelsByModelBrandEditionAndFuelType(String brand, String model, String edition, FuelType fuelType);
    List<RegisteredVehicle> getVehicles();

    Vehicle registerVehicle(String brand,
                            String date,
                            String modelName,
                            String edition,
                            String fuelTypeString,
                            String energyLabelString);
    RegisteredVehicle newVehicle(long vehicleID,
                                 long userID,
                                 String license,
                                 InputStream uploadedInputStream,
                                 FormDataContentDisposition fileDetails);
    RegisteredVehicle newVehicle(String brand,
                                 String date,
                                 String modelName,
                                 String edition,
                                 String fuelTypeString,
                                 String energyLabelString,
                                 long userID,
                                 String license,
                                 InputStream uploadedInputStream,
                                 FormDataContentDisposition fileDetails);

}