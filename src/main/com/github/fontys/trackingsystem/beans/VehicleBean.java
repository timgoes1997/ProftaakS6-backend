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
import javax.ws.rs.core.Response;
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
    public List<RegisteredVehicle> getVehiclesFromUser() {
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
    public List<String> getBrands() {
        return vehicleService.getBrands();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands/{brand}")
    public List<Vehicle> getVehiclesByBrand(@PathParam("brand") String brand) {
        return vehicleService.getVehiclesByBrand(brand);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/models")
    public List<String> getVehicleModelsByBrand(@PathParam("brand") String brand) {
        return vehicleService.getVehicleModelsByBrand(brand);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/editions")
    public List<String> getEditionsByModelAndBrand(@PathParam("brand") String brand, @PathParam("model") String model) {
        return vehicleService.getEditionsByModelAndBrand(brand, model);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/fueltypes")
    public List<String> getFuelTypesByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        return vehicleService.getFuelTypesByModelBrandAndEdition(brand, model, edition);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/energylabels")
    public List<String> getEnergyLabelsByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        return vehicleService.getEnergyLabelsByModelBrandAndEdition(brand, model, edition);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/{fuelType}/energylabels")
    public List<String> getEnergyLabelsByModelBrandEditionAndFuelType(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition, @PathParam("fuelType") FuelType fuelType) {
        return vehicleService.getEnergyLabelsByModelBrandEditionAndFuelType(brand, model, edition, fuelType);
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registered")
    public List<RegisteredVehicle> getVehicles() {
        return vehicleService.getVehicles();
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/ownership/{license}")
    public Response getOwnerShip(@PathParam("license") String license){
        File poo = vehicleService.getProofOfOwnership(license);
        String fileName = poo.getName();
        return Response.ok((Object)poo)
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .build();
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
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/destroy")
    public RegisteredVehicle destroyVehicle(@FormParam("license") String license) {
        return vehicleService.destroyVehicle(license);
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
