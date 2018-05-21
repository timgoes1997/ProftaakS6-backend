package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.tracking.Location;

import java.util.Dictionary;
import java.util.List;

public interface GenerationService {

    Bill generateBillForRoute(List<Location> route);

    List<Bill> generateBillsForVehicle(int vehicleId);
}
