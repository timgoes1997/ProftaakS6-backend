package com.github.fontys.international;

import com.github.fontys.trackingsystem.dao.BillDaoImpl;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.beans.VehicleServiceImpl;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.RouteTransformer;
import com.nonexistentcompany.domain.RichRoute;
import com.nonexistentcompany.domain.Route;
import com.nonexistentcompany.queue.RichRouteHandler;
import com.nonexistentcompany.queue.RouteHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;


public class ReceiveDriven {

    private static RouteEngine engine = new RouteEngine("NL");
    private static RouteTransformer routeTransformer;

    public static void main(String[] args) throws IOException, TimeoutException {
        // region Listen for cars driven
        routeTransformer = new RouteTransformerImpl();

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

                VehicleServiceImpl vehicleService = new VehicleServiceImpl();
                BillDAO billDAO = new BillDaoImpl();

                //Generate Bill
                Bill bill = new Bill();

                //TODO: Get last route for vehicle (?)
                bill.setStartDate(null); //TODO: Get the startdate from the first location in the route -.-
                bill.setEndDate(null); //TODO: Get the enddate from the last location in the route ---___---
                bill.setEndOfMonthBill(false);
                bill.setStatus(PaymentStatus.OPEN);
                bill.setAlreadyPaid(BigDecimal.ZERO);

                bill.setMileage(richRoute.getDistance());
                bill.setPrice(BigDecimal.valueOf(richRoute.getPrice()));
                bill.setRegisteredVehicle(vehicleService.getRegisteredVehicle(richRoute.getLicense()));

                billDAO.create(bill);

                System.out.println(String.format("Received rich route from '%s'. We should be '%s'",
                        richRoute.getDrivenInCountry(),
                        richRoute.getOriginCountry()));

            }
        };

        engine.listenForRichRoutes(richRouteHandler);
        // endregion Listen for new payment requests
    }
}