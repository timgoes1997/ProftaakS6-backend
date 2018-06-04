package com.github.fontys.trackingsystem.beans;

import com.github.fontys.international.RouteTransformerGermany;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.RouteTransformer;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

@Stateless
public class EuroBean {

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
        // region Listen for cars driven
        routeTransformer = new RouteTransformerGermany();

        // Define a handler for incoming routes
        RouteHandler routeHandler = new RouteHandler() {
            @Override
            public void handleRoute(Route route) throws IOException, TimeoutException {
                System.out.println(String.format("Got a new route from %s!", route.getOriginCountry()));

                // Transform route into route with rates
                RichRoute richRoute = routeTransformer.generateRichRoute(route);
                engine.sendRichRouteToCountry(richRoute);
            }
        };

        // Start listening for routes driven in my country
        engine.listenForRoutesInMyCountry(routeHandler);
        // endregion Listen for cars driven

        // region Listen for new rich routes
        RichRouteHandler richRouteHandler = new RichRouteHandler() {
            @Override
            public void handleRichRoute(RichRoute richRoute) {
                Bill bill = new Bill();

                //TODO: Get last route for vehicle (?)

                RegisteredVehicle registeredVehicle = vehicleService.getRegisteredVehicle(richRoute.getLicense());

                bill.setStartDate(null); //TODO: Get the startdate from the first location in the route -.-
                bill.setEndDate(null); //TODO: Get the enddate from the last location in the route ---___---
                bill.setEndOfMonthBill(false);
                bill.setStatus(PaymentStatus.OPEN);
                bill.setAlreadyPaid(BigDecimal.ZERO);

                bill.setMileage(richRoute.getDistance());
                bill.setPrice(BigDecimal.valueOf(richRoute.getPrice()));
                bill.setRegisteredVehicle(vehicleService.getRegisteredVehicle(richRoute.getLicense()));

                Bill b = billService.createBill(bill);

                System.out.println(String.format("Created bill: %s", b));

                System.out.println(String.format("Received rich route from '%s'. We should be '%s'",
                        richRoute.getDrivenInCountry(),
                        richRoute.getOriginCountry()));

            }
        };

        engine.listenForRichRoutes(richRouteHandler);
        // endregion Listen for new payment requests

        return Response.ok().build();
    }
}
