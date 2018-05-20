package com.github.fontys.helper;

import com.github.fontys.trackingsystem.DummyDataGenerator;
import com.github.fontys.trackingsystem.dao.*;
import com.github.fontys.trackingsystem.dao.interfaces.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceHelper {

    private static AccountDAO accountDAO;
    private static BillDAO billDAO;
    private static LocationDAO locationDAO;
    private static RegisteredVehicleDAO registeredVehicleDAO;
    private static TrackedVehicleDAO trackedVehicleDAO;
    private static TradeDAO tradeDAO;
    private static UserDAO userDAO;
    private static VehicleDAO vehicleDAO;



    private static final EntityManager entityManager;
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProftaakTest");
        entityManager = emf.createEntityManager();
    }
    public static EntityManager getEntityManager() {
        return entityManager;
    };

    public static void cleanDataBase(){
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM ACCOUNT");
        entityManager.createQuery("DELETE FROM BILL");
        entityManager.createQuery("UPDATE TRACKED_VEHICLE SET REGISTERED_VEHICLE = null");
        entityManager.createQuery("UPDATE TRACKED_VEHICLE SET LAST_LOCATION = null");
        entityManager.createQuery("UPDATE TRACKED_VEHICLE SET HARDWARE = null");
        entityManager.createQuery("UPDATE TRANSFER SET CURRENT_OWNER_ID = null");
        entityManager.createQuery("UPDATE TRANSFER SET TRANSFER_OWNER_ID = null");
        entityManager.createQuery("UPDATE TRANSFER SET REGISTERED_VEHICLE_ID = null");
        entityManager.createQuery("DELETE FROM REGISTERED_VEHICLE");
        entityManager.createQuery("DELETE FROM TRACKEDVEHICLE_LOCATIONS");
        entityManager.createQuery("DELETE FROM TRACKED_VEHICLE");
        entityManager.createQuery("DELETE FROM CUSTOMER");
        entityManager.createQuery("DELETE FROM HARDWARE");
        entityManager.createQuery("DELETE FROM VEHICLE");
        entityManager.createQuery("DELETE FROM TRACKING_LOCATION");
        entityManager.createQuery("DELETE FROM TRANSFER");
        entityManager.createQuery("DELETE FROM RATE");
        entityManager.createQuery("DELETE FROM REGION");
        entityManager.getTransaction().commit();
    }

    public static void generateDummyData(){
        DummyDataGenerator dataGenerator = new DummyDataGenerator();
        dataGenerator.setAccountDAO(getAccountDAO());
        dataGenerator.setBillDAO(getBillDAO());
        dataGenerator.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        dataGenerator.setUserDAO(getUserDAO());
        dataGenerator.setVehicleDAO(getVehicleDAO());
        dataGenerator.setEm(entityManager);
        dataGenerator.init();
    }

    public static AccountDAO getAccountDAO(){
        if(accountDAO != null) return accountDAO;
        AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
        accountDAOImpl.setEntityManager(entityManager);
        accountDAO = accountDAOImpl;
        return accountDAO;
    }

    public static BillDAO getBillDAO() {
        if(billDAO != null) return billDAO;
        BillDaoImpl billDAOImpl = new BillDaoImpl();
        billDAOImpl.setEntityManager(entityManager);
        billDAO = billDAOImpl;
        return billDAO;
    }

    public static LocationDAO getLocationDAO() {
        if(locationDAO != null) return locationDAO;
        LocationDAOImpl locationDAOImpl = new LocationDAOImpl();
        locationDAOImpl.setEntityManager(entityManager);
        locationDAO = locationDAOImpl;
        return locationDAO;
    }

    public static RegisteredVehicleDAO getRegisteredVehicleDAO() {
        if(registeredVehicleDAO != null) return registeredVehicleDAO;
        RegisteredVehicleDAOImpl registeredVehicleDAOImpl = new RegisteredVehicleDAOImpl();
        registeredVehicleDAOImpl.setEntityManager(entityManager);
        registeredVehicleDAO = registeredVehicleDAOImpl;
        return registeredVehicleDAO;
    }

    public static TrackedVehicleDAO getTrackedVehicleDAO() {
        if(trackedVehicleDAO != null) return trackedVehicleDAO;
        TrackedVehicleDAOImpl trackedVehicleDAOImpl = new TrackedVehicleDAOImpl();
        trackedVehicleDAOImpl.setEntityManager(entityManager);
        trackedVehicleDAO = trackedVehicleDAOImpl;
        return trackedVehicleDAO;
    }

    public static TradeDAO getTradeDAO() {
        if(tradeDAO != null) return tradeDAO;
        TradeDAOImpl tradeDAOImpl = new TradeDAOImpl();
        tradeDAOImpl.setEntityManager(entityManager);
        tradeDAO = tradeDAOImpl;
        return tradeDAO;
    }

    public static UserDAO getUserDAO() {
        if(userDAO != null) return userDAO;
        UserDAOImpl userDAOImpl= new UserDAOImpl();
        userDAOImpl.setEntityManager(entityManager);
        userDAO = userDAOImpl;
        return userDAO;
    }

    public static VehicleDAO getVehicleDAO() {
        if(vehicleDAO != null) return vehicleDAO;
        VehicleDAOImpl vehicleDAOImpl = new VehicleDAOImpl();
        vehicleDAOImpl.setEntityManager(entityManager);
        vehicleDAO = vehicleDAOImpl;
        return vehicleDAO;
    }
}
