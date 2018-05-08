package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.DummyDataGenerator;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.util.Nonbinding;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RequestScoped
@Path("/vehicles")
public class VehicleBean {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private DummyDataGenerator db;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currentUser")
    public Response getVehiclesFromUser() {
        try {
            User user = (User)currentUser;
            List<RegisteredVehicle> registeredVehicles = registeredVehicleDAO.findByUser(user.getId());
            GenericEntity<List<RegisteredVehicle>> list = new GenericEntity<List<RegisteredVehicle>>(registeredVehicles){};
            return (registeredVehicles.size() > 0)
                    ? Response.ok(list).build() : Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getVehicle(@PathParam("id") int id) {
        try {
            Vehicle vehicle = vehicleDAO.find(id);
            return Response.status(Response.Status.FOUND).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands")
    public Response getBrands() {
        List<String> brands = vehicleDAO.getBrands();
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(brands) { };
        return (brands.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands/{brand}")
    public Response getVehiclesByBrand(@PathParam("brand") String brand) {
        List<Vehicle> vehicles = vehicleDAO.findByBrand(brand);
        GenericEntity<List<Vehicle>> list = new GenericEntity<List<Vehicle>>(vehicles) { };
        return (vehicles.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/models")
    public Response getVehicleModelsByBrand(@PathParam("brand") String brand) {
        List<String> brands = vehicleDAO.findModelsByBrand(brand);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(brands) { };
        return (brands.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/editions")
    public Response getEditionsByModelAndBrand(@PathParam("brand") String brand, @PathParam("model") String model) {
        List<String> models = vehicleDAO.findEditionsByModelAndBrand(brand, model);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(models) { };
        return (models.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/fueltypes")
    public Response getFuelTypesByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        List<String> fuelTypes = vehicleDAO.findFuelTypesByModelBrandAndEdition(brand, model, edition);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) { };
        return (fuelTypes.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/energylabels")
    public Response getEnergyLabelsByModelBrandAndEdition(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition) {
        List<String> fuelTypes = vehicleDAO.findEnergyLabelsByModelBrandAndEdition(brand, model, edition);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) { };
        return (fuelTypes.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{brand}/{model}/{edition}/{fuelType}/energylabels")
    public Response getEnergyLabelsByModelBrandEditionAndFuelType(@PathParam("brand") String brand, @PathParam("model") String model, @PathParam("edition") String edition, @PathParam("fuelType") FuelType fuelType) {
        List<String> fuelTypes = vehicleDAO.findEnergyLabelsByModelBrandEditionAndFuelType(brand, model, edition, fuelType);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) { };
        return (fuelTypes.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/registered")
    public Response getVehicles() {
        List<RegisteredVehicle> vehicles;

        // RETURN OWN VEHICLES FOR;
        // CUSTOMERS
        if (currentUser.getPrivilege() == Role.CUSTOMER) {
            vehicles = ((User)currentUser).getRegisteredVehicles();
        } else {
            // FOR ANY OTHER ROLE, RETURN ALL
            vehicles = registeredVehicleDAO.getAll();
        }
        GenericEntity<List<RegisteredVehicle>> list = new GenericEntity<List<RegisteredVehicle>>(vehicles) { };
        return (vehicles.size() > 0)
                ? Response.ok(list).build() : Response.noContent().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/create/vehicle")
    public Response registerVehicle(@FormParam("brand") String brand,
                                    @FormParam("buildDate") String date,
                                    @FormParam("modelName") String modelName,
                                    @FormParam("edition") String edition,
                                    @FormParam("fuelType") String fuelTypeString,
                                    @FormParam("energyLabel") String energyLabelString
                                    ) {
        FuelType fuelType = FuelType.valueOf(fuelTypeString);
        EnergyLabel energyLabel = EnergyLabel.valueOf(energyLabelString);

        Date inDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            inDate = sdf.parse(date);
            Vehicle v = vehicleDAO.find(modelName, edition, fuelType, energyLabel);
            if (v != null) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        } catch (EJBException e) { //Expects a NoResultException but that is hidden in EJBException
            vehicleDAO.create(new Vehicle(brand, inDate, modelName, edition, fuelType, energyLabel));
            Vehicle v = vehicleDAO.find(modelName, edition, fuelType, energyLabel);
            return Response.status(Response.Status.CREATED).entity(v).build();
        } catch (ParseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

        return Response.status(Response.Status.EXPECTATION_FAILED).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response newVehicle(@FormDataParam("vehicle") long vehicleID,
                               @FormDataParam("user") long userID,
                               @FormDataParam("licenseplate") String license,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
        Vehicle v;
        try {
            v = vehicleDAO.find(vehicleID);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }

        User u;
        try {
            u = userDAO.find(userID);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }

        String uploadedFileLocation = System.getProperty("user.dir") + "//files//";

        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            return Response.notModified("No file given").build();
        }

        File f = getNewFile(uploadedFileLocation, extension);//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String location = uploadedFileLocation + f.getName();

        if (v == null && u == null) return Response.notModified("Entity and vehicle are both null").build();

        RegisteredVehicle cv = new RegisteredVehicle(u, license, v, location);

        return createRegisteredVehicle(cv);
    }

    @POST
    @Path("/register/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response newVehicle(@FormDataParam("brand") String brand,
                               @FormDataParam("buildDate") String date,
                               @FormDataParam("modelName") String modelName,
                               @FormDataParam("edition") String edition,
                               @FormDataParam("fuelType") String fuelTypeString,
                               @FormDataParam("energyLabel") String energyLabelString,
                               @FormDataParam("user") long userID,
                               @FormDataParam("licenseplate") String license,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
        FuelType fuelType = FuelType.valueOf(fuelTypeString);
        EnergyLabel energyLabel = EnergyLabel.valueOf(energyLabelString);

        Date inDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Vehicle v = null;
        try {
            inDate = sdf.parse(date);
            v = vehicleDAO.find(brand, inDate, modelName, edition, fuelType, energyLabel);
        }catch (EJBException e) { //Expects a NoResultException but that is hidden in EJBException
            v = new Vehicle(brand, inDate, modelName, edition, fuelType, energyLabel);
        }

        User u;
        try {
            u = userDAO.find(userID);
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }

        String uploadedFileLocation = System.getProperty("user.dir") + "//files//";

        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            return Response.notModified("No file given").build();
        }

        File f = getNewFile(uploadedFileLocation, extension);//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String location = uploadedFileLocation + f.getName();

        if (v == null && u == null) return Response.notModified("Entity and vehicle are both null").build();

        RegisteredVehicle cv = new RegisteredVehicle(u, license, v, location);

        return createRegisteredVehicle(cv);
    }

    private Response createRegisteredVehicle(RegisteredVehicle cv) {
        try {
            registeredVehicleDAO.create(cv);
            RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByLicense(cv.getLicensePlate());
            return Response.status(200).entity(registeredVehicle).build();
        } catch (Exception e) {
            return Response.serverError().entity(e).build();
        }
    }

    private File getNewFile(String uploadFileLocation, String fileType) {
        Random random = new SecureRandom();
        File f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        while (f.exists()) {
            f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        }
        return f;
    }

    private String getRandomFileName(Random random, String fileType) {
        return new BigInteger(130, random).toString(32) + "." + fileType;
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             File f) {
        try {
            OutputStream out;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(f);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
