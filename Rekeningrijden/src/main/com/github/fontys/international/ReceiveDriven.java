package com.github.fontys.international;

import com.github.fontys.trackingsystem.dao.BillDaoImpl;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.beans.LocationServiceImpl;
import com.github.fontys.trackingsystem.services.beans.VehicleServiceImpl;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.nonexistentcompany.RouteEngine;
import com.nonexistentcompany.RouteTransformer;
import com.nonexistentcompany.domain.RichRoute;
import com.nonexistentcompany.domain.Route;
import com.nonexistentcompany.queue.RichRouteHandler;
import com.nonexistentcompany.queue.RouteHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class ReceiveDriven {

    public static void main(String[] args) throws IOException, TimeoutException {

    }
}