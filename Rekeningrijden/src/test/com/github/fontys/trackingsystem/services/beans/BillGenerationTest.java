package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Route;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.services.interfaces.RouteService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

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
    public void generateRoutesBill() {
        PersistenceHelper.generateDummyData();
        List<Location> locationList = PersistenceHelper.getDataGenerator().generateRuhrBerlin();
        RouteService routeService = PersistenceHelper.getRouteService();
        List<Route> routes = routeService.generateRoutes(locationList, EnergyLabel.A);

        assertTrue(routes.size() > 0);
    }

}