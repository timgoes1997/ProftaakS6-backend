package com.github.fontys.trackingsystem.beans;

import com.github.fontys.international.RouteTransformerGermany;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.RouteTransformer;
import com.nonexistentcompany.domain.RichRoute;
import com.nonexistentcompany.domain.Route;
import com.nonexistentcompany.queue.RichRouteHandler;
import com.nonexistentcompany.queue.RouteHandler;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

@RequestScoped
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

    private RouteTransformer routeTransformer = new RouteTransformerGermany();
    private RouteEngine engine = new RouteEngine("DE");

    @POST
//    @EasySecurity(requiresUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{license}")
    public Response handleRoute(@PathParam("license") String license,
                                               @FormParam("startdate") String startdate,
                                               @FormParam("enddate") String enddate) throws IOException, TimeoutException {
        // format (IMPORTANT): yyyy-MM-dd hh:mm:ss
        generationService.handleLastRoute(startdate, enddate, license);
        return Response.ok().build();
    }
}
