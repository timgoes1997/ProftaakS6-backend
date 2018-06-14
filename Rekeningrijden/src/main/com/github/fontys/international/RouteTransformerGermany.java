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
//        List<EULocation> locations = route.getLocationList();
//        int distance = (int) generationService.calculateDistance(locations);
//
//        // Rate in cents per kilometer
//        // TODO: Calculate price per region, create Rate objects, calculate totalprice
//        int rate = 20;
//        Rate rateObject = new Rate(0, rate, distance);
//        rates.add(rateObject);
//
//        // Price in cents
//        int price = (int) generationService.generatePriceWithSingleRate(distance, rate);
//
//        // Vats in percentages
//        int vats = 21;
//        return new RichRoute(route.getOriginCountry(), route.getDrivenInCountry(), distance, price, vats, route.getLicensePlate(), "DE", rates)
//

        return null;
    }
}