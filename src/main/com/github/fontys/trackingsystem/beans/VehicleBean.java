package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.CustomerVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RequestScoped
@Path("/vehicles")
public class VehicleBean {

    @Inject
    private DatabaseMock db;

    @Inject
    private VehicleModelDAO vehicleModelDAO;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private CustomerVehicleDAO customerVehicleDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getVehicle(@PathParam("id") int id) {
        try {
            Vehicle vehicle = vehicleDAO.find(id);
            return Response.status(Response.Status.FOUND).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands")
    public Response getBrands() {
        List<String> brands = vehicleDAO.getBrands();
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(brands) {};
        return Response.ok(brands).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands/{brand}")
    public Response getVehicles(@PathParam("brand") String brand) {
        List<Vehicle> vehicles = vehicleDAO.findByBrand(brand);
        GenericEntity<List<Vehicle>> list = new GenericEntity<List<Vehicle>>(vehicles) {};
        if (vehicles.size() > 0) {
            return Response.ok(list).build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/active/all")
    public Response getVehicles() {
        List<CustomerVehicle> vehicles = new ArrayList<>();
        GenericEntity<List<CustomerVehicle>> list = new GenericEntity<List<CustomerVehicle>>(vehicles) {};
        if (vehicles.size() > 0) {
            return Response.ok(list).build();
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/vehicle")
    public Response registerVehicle(@FormParam("brand") String brand,
                                    @FormParam("model") Long modelID,
                                    @FormParam("buildDate") String date) {
        VehicleModel vm;
        try{
            vm = vehicleModelDAO.find(modelID);
        }catch (EJBException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }

        Date inDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        try{
            inDate = sdf.parse(date);
            Vehicle v = vehicleDAO.find(brand, vm.getId(), inDate);
            if(v != null){
                return Response.status(Response.Status.CONFLICT).build();
            }
        } catch (ParseException|EJBException e) { //Expects a NoResultException but that is hidden in EJBException
            if(e instanceof EJBException){
                vehicleDAO.create(new Vehicle(brand, vm, inDate));
                Vehicle v = vehicleDAO.find(brand, vm.getId(), inDate);
                return Response.status(Response.Status.CREATED).entity(v).build();
            }else{
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
            }
        }

        return Response.status(Response.Status.EXPECTATION_FAILED).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/model")
    public Response registerModel(@FormParam("modelName") String modelName,
                                  @FormParam("edition") String edition,
                                  @FormParam("fuelType") FuelType fuelType,
                                  @FormParam("energyLabel") EnergyLabel energyLabel) {
        try {
            VehicleModel vm = vehicleModelDAO.find(modelName, edition, fuelType, energyLabel);
            if (vm != null) {
                return Response.status(Response.Status.CONFLICT).build();
            } else {
                vehicleModelDAO.create(new VehicleModel(modelName, edition, fuelType, energyLabel));
                VehicleModel vm2 = vehicleModelDAO.find(modelName, edition, fuelType, energyLabel);
                return Response.status(Response.Status.CREATED).entity(vm2).build();
            }
        } catch (EJBException e) {
            vehicleModelDAO.create(new VehicleModel(modelName, edition, fuelType, energyLabel));
            VehicleModel vm = vehicleModelDAO.find(modelName, edition, fuelType, energyLabel);
            return Response.status(Response.Status.CREATED).entity(vm).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/model/find/{id}")
    public Response getModel(@PathParam("id") Long id) {
        try {
            VehicleModel vm = vehicleModelDAO.find(id);
            return Response.status(Response.Status.FOUND).entity(vm).build();
        } catch (Exception e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/model/find/")
    public Response getModel(@QueryParam("modelName") String modelName, @QueryParam("edition") String edition, @QueryParam("fuelType") FuelType fuelType, @QueryParam("energyLabel") EnergyLabel energyLabel) {
        try {
            VehicleModel vm = vehicleModelDAO.find(modelName, edition, fuelType, energyLabel);
            return Response.ok(vm).build();
        } catch (Exception e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/model/find/name/{name}")
    public Response getModel(@PathParam("name") String name) {
        try {
            List<VehicleModel> vm = vehicleModelDAO.findModelsByModelName(name);
            return Response.ok(vm).build();
        } catch (Exception e) {
            return Response.noContent().build();
        }
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
        try{
            v = vehicleDAO.find(vehicleID);
        }catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }

        User u;
        try{
            u = userDAO.find(userID);
        }catch(Exception e){
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

        if(v == null && u == null) return Response.notModified("Entity and vehicle are both null").build();

        CustomerVehicle cv = new CustomerVehicle(u, license, v, location);

        try{
            customerVehicleDAO.create(cv);
            CustomerVehicle customerVehicle = customerVehicleDAO.findByLicense(cv.getLicensePlate());
            return Response.status(200).entity(customerVehicle).build();
        }catch (Exception e){
            return Response.serverError().entity(e).build();
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {

        String uploadedFileLocation = "D://School//S6//Proftaak//Git//Test//";
        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            return Response.notModified("No file given").build();
        }

        File f = new File(uploadedFileLocation + fileDetails.getFileName());//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String output = "File uploaded to : " + uploadedFileLocation + f.getName();

        return Response.status(200).entity(output).build();
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
        String newFileName = new BigInteger(130, random).toString(32) + "." + fileType;
        return newFileName;
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
