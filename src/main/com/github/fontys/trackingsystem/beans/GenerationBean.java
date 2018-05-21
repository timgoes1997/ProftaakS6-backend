package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/generate")
public class GenerationBean {

    @Inject
    GenerationService generationService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/bill/{registeredvehicleid}")
    public List<Bill> generateBillsForVehicle(@PathParam("registeredvehicleid") long registeredVehicleId) {
        return generationService.generateBillsForVehicle(registeredVehicleId);
    }
}