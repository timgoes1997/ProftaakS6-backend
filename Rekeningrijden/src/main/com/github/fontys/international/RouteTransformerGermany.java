package com.github.fontys.international;

import com.github.fontys.trackingsystem.services.beans.GenerationServiceImpl;
import com.nonexistentcompany.RouteTransformer;
import com.nonexistentcompany.domain.EULocation;
import com.nonexistentcompany.domain.Rate;
import com.nonexistentcompany.domain.RichRoute;
import com.nonexistentcompany.domain.Route;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RouteTransformerGermany extends RouteTransformer {

    @Inject
    private GenerationServiceImpl generationService;

    @Override
    public RichRoute generateRichRoute(Route route) {
        List<Rate> rates = new ArrayList<>();
        List<EULocation> locations = route.getLocationList();
        int distance = (int) generationService.calculateDistance(locations);

        // Rate in cents per kilometer
        // TODO: Calculate price per region, create Rate objects, calculate totalprice
        int rate = 20;
        Rate rateObject = new Rate(0, rate, distance);
        rates.add(rateObject);

        // Price in cents
        int price = (int) generationService.generatePriceWithSingleRate(distance, rate);

        // Vats in percentages
        int vats = 21;
        return new RichRoute(route.getOriginCountry(), route.getDrivenInCountry(), distance, price, vats, route.getLicensePlate(), "DE", rates);
    }
}