package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.VehicleModelDAOImpl;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void TestAAddVehicleModel(){
        em.getTransaction().begin();
        vmDao.create(new VehicleModel("Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A));
        vmDao.create(new VehicleModel("Model X", "P90D", FuelType.ELECTRIC, EnergyLabel.A));
        em.getTransaction().commit();;
    }

    @Test
    public void TestBFindAllModels(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findAllModels();
        assertTrue(vModels.size() > 0);
        vm = vModels.get(0);

        em.getTransaction().commit();
    }

    @Test
    public void TestCFindModelByName(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByModelName("Model X");
        assertTrue(vModels.size() > 0);

        em.getTransaction().commit();
    }
}