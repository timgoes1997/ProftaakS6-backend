package com.github.fontys.international;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.trackingsystem.services.beans.GenerationServiceImpl;
import com.nonexistentcompany.lib.RouteTransformer;
import com.nonexistentcompany.lib.domain.EULocation;
import com.nonexistentcompany.lib.domain.ForeignRoute;
import com.nonexistentcompany.lib.domain.RichRoute;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RouteTransformerGermany extends RouteTransformer {

    @Inject
    private GenerationServiceImpl generationService;

    @Override
    public RichRoute generateRichRoute(ForeignRoute route, String ownCountry) {
        List<Rate> rates = new ArrayList<>();
        List<List<EULocation>> tripLocations = route.getTrips();

        for(List<EULocation> locations: tripLocations) {
            locations.sort(EULocation::compareTo);
        }

        // Calculate distance of domestic route
        int distance = 0;
        for(List<EULocation> locations:  tripLocations){
            distance += (int) generationService.calculateDistance(locations);
        }

        // Rate in cents per kilometer
        // TODO: Calculate price per region, create Rate objects, calculate totalprice
        int rate = 20;
//        Rate rateObject = new Rate(0, rate, distance);
//        rates.add(rateObject);

        // Price in cents
        double price = generationService.generatePriceWithSingleRate(distance, rate);

        // Vats in percentages
        int vats = 21;
        return new RichRoute(route.getId(), route.getOrigin(), price, distance, vats, null);
    }
}