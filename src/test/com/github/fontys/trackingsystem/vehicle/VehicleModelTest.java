package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.VehicleModelDAOImpl;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.*;

public class VehicleModelTest {

    private EntityManager em;
    private VehicleModel vm;
    private VehicleModelDAO vmDao;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        VehicleModelDAOImpl vehicleModelDAOImpl = new VehicleModelDAOImpl();
        vehicleModelDAOImpl.setEntityManager(em);
        vmDao = vehicleModelDAOImpl;
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

    @Test
    public void FindAllModels(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findAllModels();


        em.getTransaction().commit();
    }
}