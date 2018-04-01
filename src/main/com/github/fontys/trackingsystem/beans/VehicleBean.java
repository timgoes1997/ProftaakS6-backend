package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@RequestScoped
@Path("/vehicle")
public class VehicleBean {

    @Inject
    private DatabaseMock db;

    @Inject
    private VehicleModelDAO vehicleModelDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getVehicle(@PathParam("id") int id) {
        for (CustomerVehicle cv : db.getCustomerVehicles()) {
            if (cv.getId() == id) {
                return Response.ok(cv).build();
            }
        }
        return Response.serverError().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands")
    public Response getBrands() {
        List<String> brands = db.getBrands();
        return Response.ok(brands).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/brands/{brand}")
    public Response getVehicles(@PathParam("brand") String brand) {
        List<Vehicle> vehicles = db.getVehiclesByBrand(brand);
        if(vehicles.size() > 0){
            return Response.ok(vehicles).build();
        } else{
          return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/")
    public Response registerVehicle(@FormParam("brand") String brand,
                                    @FormParam("model") int modelID,
                                    @FormParam("licenseplate") String license,
                                    @FormParam("buildDate") String date) {
        List<Vehicle> vehicles = db.getVehiclesByBrand(brand);

        //TODO: check for already existing vehicles of the same type and return a error when that happens.
        return Response.ok(vehicles).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/model")
    public Response registerModel(@FormParam("modelName") String modelName,
                                    @FormParam("edition") String edition,
                                    @FormParam("fuelType") FuelType fuelType,
                                    @FormParam("energyLabel") EnergyLabel energyLabel) {
        VehicleModel model = new VehicleModel(modelName, edition, fuelType, energyLabel);

        //veh
        vehicleModelDAO.create(model);

        //TODO: check for already existing vehicles of the same type and return a error when that happens.
        return Response.ok(model).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/model/find/{id}")
    public Response getModel(@PathParam("id") Long id) {
        try{
            VehicleModel vm = vehicleModelDAO.find(id);
            return Response.ok(vm).build();
        }catch (Exception e){
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/model/find/name/{name}")
    public Response getModel(@PathParam("name") String name) {
        try{
            List<VehicleModel> vm = vehicleModelDAO.findModelsByModelName(name);
            return Response.ok(vm).build();
        }catch (Exception e){
            return Response.noContent().build();
        }
    }


    @POST
    @Path("/new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response newVehicle(@FormDataParam("brand") String brand,
                               @FormDataParam("model") int modelID,
                               @FormDataParam("licenseplate") String license,
                               @FormDataParam("buildDate") String BuildDate,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
        String uploadedFileLocation = "D://School//S6//Proftaak//Git//Test//";

        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if(extension.equals("")) {
            return Response.notModified("No file given").build();
        }

        File f = getNewFile(uploadedFileLocation, extension);//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String output = "File uploaded to : " + uploadedFileLocation + f.getName();

        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {

        String uploadedFileLocation = "D://School//S6//Proftaak//Git//Test//";
        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if(extension.equals("")) {
            return Response.notModified("No file given").build();
        }

        File f = new File(uploadedFileLocation + fileDetails.getFileName());//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String output = "File uploaded to : " + uploadedFileLocation + f.getName();

        return Response.status(200).entity(output).build();
    }

    private File getNewFile(String uploadFileLocation, String fileType){
        Random random = new SecureRandom();
        File f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        while(f.exists()){
            f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        }
        return f;
    }

    private String getRandomFileName(Random random, String fileType){
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
