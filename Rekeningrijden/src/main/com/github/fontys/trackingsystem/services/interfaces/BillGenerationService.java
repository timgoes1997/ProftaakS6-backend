package com.github.fontys.trackingsystem.services.interfaces;

import com.nonexistentcompany.lib.domain.RichRoute;

import java.util.Calendar;

public interface BillGenerationService {
    void generateBill(long registeredVehicleId, Calendar startDate, Calendar endDate);
    void regenerateBill(long billId);
    void receiveRichRouteForBill(RichRoute richRoute);
}
