package com.github.fontys.trackingsystem.vehicle;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VehicleModelTest {

    private EntityManager em;
    private VehicleModel vm;
    private VehicleModelDAO vmDao;

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        vmDao.create(new VehicleModel("Polo", "GT", FuelType.DIESEL, EnergyLabel.F));
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
        assertTrue(vModels.size() == 2);

        em.getTransaction().commit();
    }

    @Test
    public void TestDFindModelByEdition(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByEdition("P90D");
        assertTrue(vModels.size() == 1);

        em.getTransaction().commit();
    }

    @Test
    public void TestEFindModelByFuelType(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByFuelType(FuelType.ELECTRIC);
        assertTrue(vModels.size() == 2);

        em.getTransaction().commit();
    }

    @Test
    public void TestFFindModelByEnergyLabel(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByEnergyLabel(EnergyLabel.A);
        assertTrue(vModels.size() == 2);

        em.getTransaction().commit();
    }

    @Test
    public void TestGFindModelByNameAndID(){
        em.getTransaction().begin();

        VehicleModel vm = vmDao.find("Model X", "P100D", FuelType.ELECTRIC, EnergyLabel.A);
        assertNotNull(vm);
        VehicleModel vm2 = vmDao.find(vm.getId());
        assertNotNull(vm2);

        em.getTransaction().commit();
    }

    @Test
    public void TestHFindModelByNameAndEdition(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByNameAndEdition("Model X", "P100D");
        assertTrue(vModels.size() == 1);

        em.getTransaction().commit();
    }

    @Test
    public void TestIEditModel(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByNameAndEdition("Model X", "P100D");
        assertTrue(vModels.size() == 1);
        VehicleModel vm = vModels.get(0);
        String modelName = "P85D";
        vm.setModelName(modelName);
        vmDao.edit(vm);
        em.getTransaction().commit();

        VehicleModel vm2 = vmDao.find(vm.getId());
        assertNotNull(vm2);
        assertEquals(vm2.getModelName(), modelName);

        List<VehicleModel> vModels2 = vmDao.findModelsByNameAndEdition("Model X", "P100D");
        assertTrue(vModels2.size() == 0);
    }

    @Test
    public void TestJRemoveModel(){
        em.getTransaction().begin();

        List<VehicleModel> vModels = vmDao.findModelsByNameAndEdition("Model X", "P90D");
        assertTrue(vModels.size() == 1);
        VehicleModel vm = vModels.get(0);
        vmDao.remove(vm);
        em.getTransaction().commit();

        exception.expect(NoResultException.class);
        VehicleModel vm2 = vmDao.find(vm.getId());
    }
}