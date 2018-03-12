package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

@RequestScoped
@Path("/vehicle")
public class VehicleBean {

    @Inject
    private DatabaseMock db;

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
        List<Vehicle> vehicles = db.getVehicles(brand);
        if(vehicles.size() > 0){
            return Response.ok(vehicles).build();
        } else{
          return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/new/")
    public Response registerVehicle(@FormParam("brand") String brand, @FormParam("model") int modelID, @FormParam("licenseplate") String license, @FormParam("buildDate") String date) {
        List<Vehicle> vehicles = db.getVehicles(brand);

        //TODO: check for already existing vehicles of the same type and return a error when that happens.
        return Response.ok(vehicles).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {

        String uploadedFileLocation = "D://School//S6//Proftaak//Git//Test//" + fileDetails.getFileName();

        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded to : " + uploadedFileLocation;

        return Response.status(200).entity(output).build();
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
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
