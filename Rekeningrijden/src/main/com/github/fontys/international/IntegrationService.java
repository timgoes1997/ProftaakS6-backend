package com.github.fontys.international;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.payment.RouteDetail;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.international.RouteTransformerGermany;
import com.github.fontys.trackingsystem.services.interfaces.BillGenerationService;
import com.github.fontys.trackingsystem.services.interfaces.RouteService;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.RouteTransformer;
import com.nonexistentcompany.lib.domain.EULocation;
import com.nonexistentcompany.lib.domain.ForeignRoute;
import com.nonexistentcompany.lib.domain.RichRoute;
import com.nonexistentcompany.lib.domain.RichRouteDetail;
import com.nonexistentcompany.lib.queue.RichRouteHandler;
import com.nonexistentcompany.lib.queue.RouteHandler;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Startup
@Singleton
public class IntegrationService {

    private RouteTransformer routeTransformer;
    private RouteEngine engine;

    @Inject
    private RouteService routeService;

    @Inject
    private BillGenerationService billGenerationService;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init(){
        logger.info("Hello this is our integration!");
        listenForRoutes();
        logger.info("Setup routes");
    }

    public void listenForRoutes() {
        //setup engine
        engine = new RouteEngine("DE");

        // region Listen for cars driven
        routeTransformer = new RouteTransformer() {
            @Override
            public RichRoute generateRichRoute(ForeignRoute route, String ownCountry) {
                // Vats in percentages
                int vats = 21;

                logger.info(route.getOrigin());

                List<Rate> rates = new ArrayList<>();
                List<List<EULocation>> tripLocations = route.getTrips();

                for (List<EULocation> locations : tripLocations) {
                    locations.sort(EULocation::compareTo);
                }

                List<Location> locations = routeService.convertEULocationsToLocation(route.getId(), tripLocations);
                List<Route> routes = routeService.generateRoutes(locations, EnergyLabel.H);
                BigDecimal price = routeService.getTotalPriceRoutes(routes);
                double distance = routeService.getTotalDistanceRoutes(routes);

                logger.info(price.toString());
                logger.info(String.valueOf(distance));

                List<RichRouteDetail> routeDetails = new ArrayList<>();
                for (Route r : routes) {
                    for (RouteDetail rd : r.getRouteDetails()) {
                        routeDetails.add(new RichRouteDetail(
                                rd.getRate().getKilometerPrice().doubleValue(),
                                "RouteDetails",
                                rd.getStartTime().getTimeInMillis() / 1000,
                                rd.getEndTime().getTimeInMillis() / 1000
                        ));
                    }
                }

                RichRoute r = new RichRoute(
                        route.getId(),
                        route.getOrigin(),
                        price.doubleValue(),
                        (int) distance,
                        vats,
                        routeDetails);

                logger.info(r.toString());
                return r;
            }
        };

        // Define a handler for incoming routes
        RouteHandler routeHandler = new RouteHandler() {
            @Override
            public void handleRoute(ForeignRoute route) throws IOException, TimeoutException {
                logger.info(String.format("Got a new route from %s!", route.getOrigin()));
                logger.info(String.format("Route details: '%s'", route));

//                // Transform route into route with rates
                RichRoute richRoute = routeTransformer.generateRichRoute(route, engine.getCountry());
                engine.sendRichRouteToCountry(richRoute, route.getOrigin());
            }
        };

        // Start listening for routes driven in my country
        try {
            engine.listenForRoutesInMyCountry(routeHandler);
            // endregion Listen for cars driven

            // region Listen for new rich routes
            engine.listenForRichRoutes(new RichRouteHandler() {
                @Override
                public void handleRichRoute(RichRoute richRoute) {
                    logger.info(richRoute.toString());
                    billGenerationService.receiveRichRouteForBill(richRoute);
                }
            });
            // endregion Listen for new payment requests

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
