package com.github.fontys.trackingsystem.beans;

import com.github.fontys.international.RouteTransformerGermany;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.RouteTransformer;
import com.nonexistentcompany.lib.domain.ForeignRoute;
import com.nonexistentcompany.lib.domain.RichRoute;
import com.nonexistentcompany.lib.queue.RichRouteHandler;
import com.nonexistentcompany.lib.queue.RouteHandler;

import java.math.BigDecimal;

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
            public void handleRoute(ForeignRoute route) throws IOException, TimeoutException {
                System.out.println(String.format("Got a new route from %s!", route.getOrigin()));

                // Transform route into route with rates
                RichRoute richRoute = routeTransformer.generateRichRoute(route, "NL");
                //Nog implementeren naar welke landen.
//                engine.sendRichRouteToCountry(richRoute);
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

                RegisteredVehicle registeredVehicle = vehicleService.getRegisteredVehicle(richRoute.getId());

                bill.setStartDate(null); //TODO: Get the startdate from the first location in the route -.-
                bill.setEndDate(null); //TODO: Get the enddate from the last location in the route ---___---
                bill.setEndOfMonthBill(false);
                bill.setStatus(PaymentStatus.OPEN);
                bill.setAlreadyPaid(BigDecimal.ZERO);

                bill.setMileage(richRoute.getDistance());
                bill.setPrice(BigDecimal.valueOf(richRoute.getPrice()));
                bill.setRegisteredVehicle(vehicleService.getRegisteredVehicle(richRoute.getId()));

                Bill b = billService.createBill(bill);

                System.out.println(String.format("Created bill: %s", b));

                System.out.println(String.format("Received rich route from '%s'. We should be '%s'",
                        richRoute.getOrigin()));

            }
        };

        engine.listenForRichRoutes(richRouteHandler);
        // endregion Listen for new payment requests

        return Response.ok().build();
    }
}
