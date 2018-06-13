package com.github.fontys.trackingsystem.beans;

import com.github.fontys.entities.payment.ForeignBill;
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
            public void handleRoute(ForeignRoute foreignRoute) throws IOException, TimeoutException {
                System.out.println(String.format("Got a new route from %s!", foreignRoute.getOrigin()));

                // Transform route into route with rates
                RichRoute richRoute = routeTransformer.generateRichRoute(foreignRoute, engine.getCountry());
                engine.sendRichRouteToCountry(richRoute, foreignRoute.getOrigin());
            }
        };

        // Start listening for routes driven in my country
        engine.listenForRoutesInMyCountry(routeHandler);
        // endregion Listen for cars driven

        // region Listen for new rich routes
        RichRouteHandler richRouteHandler = new RichRouteHandler() {
            @Override
            public void handleRichRoute(RichRoute richRoute) {
                ForeignBill foreignBill = new ForeignBill(
                        vehicleService.getRegisteredVehicle(richRoute.getId()),
                        BigDecimal.valueOf(richRoute.getPrice()),
                        BigDecimal.ZERO,
                        richRoute.getStartDateCalendar(),
                        richRoute.getEndDateCalendar(),
                        PaymentStatus.OPEN,
                        richRoute.getDistance()
                );

                ForeignBill b = billService.createForeignBill(foreignBill);

                System.out.println(String.format("Created foreign bill: %s", b));

                System.out.println(richRoute.toString());
            }
        };

        engine.listenForRichRoutes(richRouteHandler);
        // endregion Listen for new payment requests

        return Response.ok().build();
    }
}
