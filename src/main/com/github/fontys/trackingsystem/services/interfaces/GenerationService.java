package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.tracking.Location;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface GenerationService {

    void generateBillForRoute(long registeredVehicleId) throws IOException, TimeoutException;

    List<Bill> generateBillsForVehicle(long vehicleId);
}
