package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.List;

@RequestScoped
@Path("/vehicles")
public class VehicleBean {

    @Inject
    private VehicleService vehicleService;

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currentUser")
    public GenericEntity<List<RegisteredVehicle>> getVehiclesFromUser() {
        return vehicleService.getVehiclesFromUser();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Vehicle getVehicle(@PathParam("id") int id) {
        return vehicleService.getVehicle(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands")
    public GenericEntity<List<String>> getBrands() {
        return vehicleService.getBrands();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands/{brand}")
    public GenericEntity<List<Vehicle>> getVehiclesByBrand(@PathParam("brand") String brand) {
        return vehicleService.getVehiclesByBrand(brand);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/models")
    public GenericEntity<List<String>> getVehicleModelsByBrand(@PathParam("brand") String brand) {
        return vehicleService.getVehicleModelsByBrand(brand);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/editions")
    public GenericEntity<List<String>> getEditionsByModelAndBrand(@PathParam("brand") String brand, @PathParam("model") String model) {
        return vehicleService.getEditionsByModelAndBrand(brand, model);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/fueltypes")
    public GenericEntity<List<String>> getFuelTypesByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        return vehicleService.getFuelTypesByModelBrandAndEdition(brand, model, edition);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/energylabels")
    public GenericEntity<List<String>> getEnergyLabelsByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        return vehicleService.getEnergyLabelsByModelBrandAndEdition(brand, model, edition);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/{fuelType}/energylabels")
    public GenericEntity<List<String>> getEnergyLabelsByModelBrandEditionAndFuelType(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition, @PathParam("fuelType") FuelType fuelType) {
        return vehicleService.getEnergyLabelsByModelBrandEditionAndFuelType(brand, model, edition, fuelType);
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registered")
    public GenericEntity<List<RegisteredVehicle>> getVehicles() {
        return vehicleService.getVehicles();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/create/vehicle")
    public Vehicle registerVehicle(@FormParam("brand") String brand,
                                   @FormParam("buildDate") String date,
                                   @FormParam("modelName") String modelName,
                                   @FormParam("edition") String edition,
                                   @FormParam("fuelType") String fuelTypeString,
                                   @FormParam("energyLabel") String energyLabelString
    ) {
        return vehicleService.registerVehicle(brand, date, modelName, edition, fuelTypeString, energyLabelString);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public RegisteredVehicle newVehicle(@FormDataParam("vehicle") long vehicleID,
                                        @FormDataParam("user") long userID,
                                        @FormDataParam("licenseplate") String license,
                                        @FormDataParam("file") InputStream uploadedInputStream,
                                        @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
        return vehicleService.newVehicle(vehicleID, userID, license, uploadedInputStream, fileDetails);
    }

    @POST
    @Path("/register/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public RegisteredVehicle newVehicle(@FormDataParam("brand") String brand,
                                        @FormDataParam("buildDate") String date,
                                        @FormDataParam("modelName") String modelName,
                                        @FormDataParam("edition") String edition,
                                        @FormDataParam("fuelType") String fuelTypeString,
                                        @FormDataParam("energyLabel") String energyLabelString,
                                        @FormDataParam("user") long userID,
                                        @FormDataParam("licenseplate") String license,
                                        @FormDataParam("file") InputStream uploadedInputStream,
                                        @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
      return vehicleService.newVehicle(brand, date, modelName, edition, fuelTypeString, energyLabelString, userID, license, uploadedInputStream, fileDetails);
    }
}
