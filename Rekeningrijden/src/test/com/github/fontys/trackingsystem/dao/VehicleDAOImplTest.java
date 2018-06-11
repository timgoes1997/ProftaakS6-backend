//package com.github.fontys.trackingsystem.dao;
//
//import com.github.fontys.helper.PersistenceHelper;
//import com.github.fontys.trackingsystem.EnergyLabel;
//import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
//import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
//import FuelType;
//import Vehicle;
//import com.github.fontys.trackingsystem.vehicle.VehicleModel;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import javax.persistence.EntityManager;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class VehicleDAOImplTest {
//
//    private EntityManager em;
//    private VehicleDAO vDao;
//    private VehicleModelDAO vmDao;
//
//    @Rule
//    public ExpectedException exception = ExpectedException.none();
//
//    @Before
//    public void setUp() throws Exception {
//        em = PersistenceHelper.getEntityManager();
//        VehicleDAOImpl vehicleDAOImpl = new VehicleDAOImpl();
//        VehicleModelDAOImpl vehicleModelDAOImpl = new VehicleModelDAOImpl();
//        vehicleModelDAOImpl.setEntityManager(em);
//        vehicleDAOImpl.setEntityManager(em);
//        vDao = vehicleDAOImpl;
//        vmDao = vehicleModelDAOImpl;
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void TestAAddVehicles(){
//        em.getTransaction().begin();
//        vDao.create(new Vehicle("Tesla", new VehicleModel("Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A), new Date()));
//        vDao.create(new Vehicle("Tesla", new VehicleModel("Model X", "P90D", FuelType.ELECTRIC, EnergyLabel.A), new Date()));
//        vDao.create(new Vehicle("Volkswagen", new VehicleModel("Polo", "GT", FuelType.DIESEL, EnergyLabel.F), new Date()));
//        em.getTransaction().commit();;
//    }
//
//    @Test
//    public void TestBGetBrands() {
//        //List<String> brands = vDao.getBrands();
//
//
//    }
//}