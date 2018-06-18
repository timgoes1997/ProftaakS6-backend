package com.github.fontys.trackingsystem.services.beans;


import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.payment.RouteDetail;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.github.fontys.international.IntegrationService;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.services.interfaces.BillGenerationService;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.RouteService;
import com.nonexistentcompany.lib.RouteEngine;
import com.nonexistentcompany.lib.domain.RichRoute;
import com.nonexistentcompany.lib.domain.RichRouteDetail;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class BillGenerationServiceImpl implements BillGenerationService {

    @Inject
    private LocationService locationService;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private RouteService routeService;

    @Inject
    private IntegrationService integrationService;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        logger.info("New BillGenerationService");
    }

    @Override
    public void generateBill(long registeredVehicleId, Calendar startDate, Calendar endDate) {
        logger.info("Generating");
        if (!registeredVehicleDAO.exists(registeredVehicleId)) {
            throw new NotFoundException("Couldn't find registered vehicle");
        }

        RegisteredVehicle registeredVehicle = registeredVehicleDAO.find(registeredVehicleId);

        List<Location> locations = locationService.getLocationsBetweenTimesByVehicleLicense(
                registeredVehicle.getLicensePlate(),
                startDate.getTime(),
                endDate.getTime());

        List<Route> routes = routeService.generateRoutes(locations, registeredVehicle.getVehicle().getEnergyLabel());

        //EU gebeuren moet hier nog.
        RouteEngine engine = integrationService.getEngine();

        Bill domesticRouteBill = new Bill(
                registeredVehicle,
                routeService.getTotalPriceRoutes(routes),
                BigDecimal.valueOf(0),
                startDate,
                endDate,
                PaymentStatus.OPEN,
                routeService.getTotalDistanceRoutes(routes),
                true,
                routes);

        // add bill
        registeredVehicle.getBills().add(domesticRouteBill);

        // persist bill
        registeredVehicleDAO.edit(registeredVehicle);
    }

    @Override
    public void regenerateBill(long billId) {

    }

    @Override
    public void receiveRichRouteForBill(RichRoute richRoute) {
        logger.info("Received RichRoute");

        if(!registeredVehicleDAO.exists(richRoute.getId())){
            throw new NotFoundException("Couldn't find given vehicle for richroute:" + richRoute.getId());
        }

        RegisteredVehicle registeredVehicle = registeredVehicleDAO.findByLicense(richRoute.getId());

        List<Location> locations = locationService.getLocationsBetweenTimesByVehicleLicense(
                richRoute.getId(),
                richRoute.getStartDateCalendar().getTime(),
                richRoute.getEndDateCalendar().getTime());

        List<RouteDetail> routeDetails = new ArrayList<>();
        for (RichRouteDetail richRouteDetail : richRoute.getDetails()) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTimeInMillis(richRouteDetail.getStart() * 1000);
            end.setTimeInMillis(richRouteDetail.getEnd() * 1000);

            double distance = routeService.getDistance(locationService.getLocationsBetweenTimesByVehicleLicense(
                    richRoute.getId(),
                    start.getTime(),
                    end.getTime()));

            routeDetails.add(new RouteDetail(
                    start, end, distance, new BigDecimal(richRouteDetail.getRate()))
            );
        }

        Route route = new Route(
                richRoute.getStartDateCalendar(),
                richRoute.getEndDateCalendar(),
                locations,
                richRoute.getDistance(),
                new BigDecimal(richRoute.getPrice()),
                routeDetails
                );
    }

    @Override
    public Calendar getFirstOfLastMonth() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        return startDate;
    }

    @Override
    public Calendar getLastOfLastMonth() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, -1);
        endDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        return endDate;
    }
}
