package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.EnergyLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

public class VehicleModelTest {

    private EntityManager em;
    private VehicleModel vm;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AddVehicleModel(){
        em.getTransaction().begin();
        vm = new VehicleModel("Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A);
        em.persist(vm);
        em.getTransaction().commit();;
    }
}