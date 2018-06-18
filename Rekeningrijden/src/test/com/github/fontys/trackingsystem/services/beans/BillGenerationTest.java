package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.services.interfaces.GenerationService;
import com.github.fontys.trackingsystem.services.interfaces.RouteService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

public class BillGenerationTest {

    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        PersistenceHelper.cleanDataBase();
    }

    @Test
    public void generateRoutesTest() {
        PersistenceHelper.generateDummyData();
        List<Location> locationList = PersistenceHelper.getDataGenerator().generateRuhrBerlin();
        RouteService routeService = PersistenceHelper.getRouteService();
        List<Route> routes = routeService.generateRoutes(locationList, EnergyLabel.A);

        assertTrue(routes.size() > 0);
    }

    @Test
    public void generateBill(){
        PersistenceHelper.generateDummyData();
        List<Location> locationList = PersistenceHelper.getDataGenerator().generateRuhrBerlin();
        GenerationService generationService = PersistenceHelper.getGenerationService();

        RegisteredVehicle registeredVehicle = PersistenceHelper.getRegisteredVehicleDAO().find(1);
        int currentBills = registeredVehicle.getBills().size();
        generationService.generateBill(1,
                new GregorianCalendar(2012, 2, 1, 0, 0),
                new GregorianCalendar(2012, 2, 1, 23, 59));

        RegisteredVehicle updatedVehicle = PersistenceHelper.getRegisteredVehicleDAO().find(1);
        int updatedBills = updatedVehicle.getBills().size();

        assertTrue(updatedBills > currentBills);
    }

}