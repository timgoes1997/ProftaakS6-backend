package com.github.fontys.trackingsystem.services.beans;


import com.github.fontys.trackingsystem.services.interfaces.BillGenerationService;
import com.nonexistentcompany.lib.domain.RichRoute;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.logging.Logger;

@Stateless
public class BillGenerationServiceImpl implements BillGenerationService {

    @Inject
    private Logger logger;

    @PostConstruct
    public void init(){
        logger.info("New BillGenerationService");
    }

    @Override
    public void generateBill(long registeredVehicleId, Calendar startDate, Calendar endDate) {

    }

    @Override
    public void regenerateBill(long billId) {

    }

    @Override
    public void receiveRichRouteForBill(RichRoute richRoute) {
        logger.info("Received RichRoute");
    }
}
