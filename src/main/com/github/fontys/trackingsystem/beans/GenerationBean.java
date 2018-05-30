package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.services.interfaces.GenerationService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RequestScoped
@Path("/generate")
public class GenerationBean {

    @Inject
    GenerationService generationService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/monthlybill/{registeredvehicleid}")
    public Response generateLastMonthsBillForVehicle(@PathParam("registeredvehicleid") long registeredVehicleId) throws IOException, TimeoutException {
        generationService.generateBillForLastMonthsRoutes(registeredVehicleId);
        return Response.ok().build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/monthlybillbyroutebills/{registeredvehicleid}")
    public Response generateLastMonthsBillByRouteBillsForVehicle(@PathParam("registeredvehicleid") long registeredVehicleId) throws IOException
    {
        generationService.generateBillByLastMonthsRouteBills(registeredVehicleId);
        return Response.ok().build();
    }

    @POST
    @Path("/routebill/{startdate}/{enddate}/{registeredvehicleid}")
    public Response generateLastRoutePriceForVehicle(@PathParam("startdate") String startDate,
                                                     @PathParam("enddate") String endDate,
                                                     @PathParam("registeredvehicleid") long registeredVehicleId) throws IOException, TimeoutException {
        generationService.generateBillsForLastRoute(startDate, endDate, registeredVehicleId);
        return Response.ok().build();
    }
}