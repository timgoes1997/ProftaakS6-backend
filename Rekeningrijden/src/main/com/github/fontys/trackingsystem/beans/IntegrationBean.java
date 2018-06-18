package com.github.fontys.trackingsystem.beans;

import com.github.fontys.international.RouteTransformerGermany;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.RouteTransformer;
import com.nonexistentcompany.lib.domain.ForeignRoute;
import com.nonexistentcompany.lib.domain.RichRoute;
import com.nonexistentcompany.lib.queue.RichRouteHandler;
import com.nonexistentcompany.lib.queue.RouteHandler;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Startup
@Singleton
public class IntegrationBean {

    private RouteTransformer routeTransformer;
    private RouteEngine engine;

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
        routeTransformer = new RouteTransformerGermany();

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
            RichRouteHandler richRouteHandler = new RichRouteHandler() {
                @Override
                public void handleRichRoute(RichRoute richRoute) {
                    logger.info(richRoute.toString());
                }
            };

            engine.listenForRichRoutes(richRouteHandler);
            // endregion Listen for new payment requests

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
