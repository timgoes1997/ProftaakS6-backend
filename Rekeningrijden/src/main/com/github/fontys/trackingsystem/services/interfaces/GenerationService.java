package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.tracking.Location;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface GenerationService {

    void generateBillForLastMonthsRoutes(long registeredVehicleId) throws IOException, TimeoutException;

    void handleLastRoute(String startDate, String endDate, String license) throws IOException, TimeoutException;

    void generateBillByLastMonthsRouteBills(long registeredVehicleId) throws IOException;

    void generateBill(long registeredVehicleId, Calendar startDate, Calendar endDate);
    void regenerateBill(long billId);

    double getDistance(List<Location> locations);
}
