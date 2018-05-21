package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.tracking.Location;

import java.util.List;

public class GenerationServiceImpl implements GenerationService {

    @Override
    public Bill generateBillForRoute(List<Location> route) {
        return null;
    }

    @Override
    public List<Bill> generateBillsForVehicle(int vehicleId) {
        return null;
    }
}
