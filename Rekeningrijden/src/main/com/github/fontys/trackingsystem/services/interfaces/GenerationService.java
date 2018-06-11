package com.github.fontys.trackingsystem.services.interfaces;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface GenerationService {

    void generateBillForLastMonthsRoutes(long registeredVehicleId) throws IOException, TimeoutException;

    void handleLastRoute(String startDate, String endDate, String license) throws IOException, TimeoutException;

    void generateBillByLastMonthsRouteBills(long registeredVehicleId) throws IOException;
}
