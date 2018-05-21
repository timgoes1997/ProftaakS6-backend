package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/generate")
public class GenerationBean {

    @Inject
    GenerationService generationService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{vehicleId}")
    public List<Bill> generateBillsForVehicle(@PathParam("vehicleId") int vehicleId) {
        return generationService.generateBillsForVehicle(vehicleId);
    }
}
