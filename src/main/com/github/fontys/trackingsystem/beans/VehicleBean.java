package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("vehicle")
public class VehicleBean {

    @EJB
    DatabaseMock db;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Vehicle getVehicle(@PathParam("id") int id){

    }
}
