package com.github.fontys.trackingsystem.beans;

import com.github.fontys.international.RouteTransformerGermany;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.RouteTransformer;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Stateless
@Path("/arrival")
public class ArrivalBean {

    @Inject
    private LocationService locationService;

    @Inject
    private VehicleService vehicleService;

    @Inject
    private BillService billService;

    @Inject
    private GenerationService generationService;

    @POST
//    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public void handleRoute(@FormParam("license") String license,
                                @FormParam("startdate") String startdate,
                                @FormParam("enddate") String enddate) throws IOException, TimeoutException {
        // format (IMPORTANT): yyyy-MM-dd hh:mm:ss
        generationService.handleLastRoute(startdate, enddate, license);
    }
}
