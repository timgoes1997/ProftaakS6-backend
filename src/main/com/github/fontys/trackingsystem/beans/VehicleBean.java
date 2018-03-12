package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.google.gson.Gson;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        for (CustomerVehicle cv : db.getVehicles()) {
            if (cv.getId() == id) {
                return Response.ok(new Gson().toJson(cv), MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.serverError().build();
    }
}
