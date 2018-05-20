package com.github.fontys.helper;

import com.github.fontys.mock.MockEmailRecoveryService;
import com.github.fontys.mock.MockEmailTradeService;
import com.github.fontys.mock.MockEmailVerificationService;
import com.github.fontys.trackingsystem.DummyDataGenerator;
import com.github.fontys.trackingsystem.dao.*;
import com.github.fontys.trackingsystem.dao.interfaces.*;
import com.github.fontys.trackingsystem.services.beans.AuthServiceImpl;
import com.github.fontys.trackingsystem.services.beans.TradeServiceImpl;
import com.github.fontys.trackingsystem.services.beans.UserServiceImpl;
import com.github.fontys.trackingsystem.services.email.EmailTradeServiceImpl;
import com.github.fontys.trackingsystem.services.file.FileServiceImpl;
import com.github.fontys.trackingsystem.services.interfaces.AuthService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceHelper {

    //DAO
    private static AccountDAO accountDAO;
    private static BillDAO billDAO;
    private static LocationDAO locationDAO;
    private static RegisteredVehicleDAO registeredVehicleDAO;
    private static TrackedVehicleDAO trackedVehicleDAO;
    private static TradeDAO tradeDAO;
    private static UserDAO userDAO;
    private static VehicleDAO vehicleDAO;

    //Services
    private static TradeServiceImpl tradeServiceImpl;
    private static AuthServiceImpl authServiceImpl;
    private static UserServiceImpl userServiceImpl;



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
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET REGISTERED_VEHICLE = null");
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET LAST_LOCATION = null");
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET HARDWARE = null");
        entityManager.createNativeQuery("UPDATE TRANSFER SET CURRENT_OWNER_ID = null");
        entityManager.createNativeQuery("UPDATE TRANSFER SET TRANSFER_OWNER_ID = null");
        entityManager.createNativeQuery("UPDATE TRANSFER SET REGISTERED_VEHICLE_ID = null");
        entityManager.createQuery("DELETE FROM REGISTERED_VEHICLE");
        entityManager.createQuery("DELETE FROM TRACKED_VEHICLE");
        //entityManager.createQuery("DELETE FROM TRACKEDVEHICLE_LOCATIONS");
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
        entityManager.getTransaction().begin();
        DummyDataGenerator dataGenerator = new DummyDataGenerator();
        dataGenerator.setAccountDAO(getAccountDAO());
        dataGenerator.setBillDAO(getBillDAO());
        dataGenerator.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        dataGenerator.setUserDAO(getUserDAO());
        dataGenerator.setVehicleDAO(getVehicleDAO());
        dataGenerator.setEm(entityManager);
        dataGenerator.init();
        entityManager.getTransaction().commit();
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

    public static TradeServiceImpl getTradeService(){
        if(tradeServiceImpl != null) return tradeServiceImpl;
        tradeServiceImpl = new TradeServiceImpl();
        tradeServiceImpl.setUserDAO(getUserDAO());
        tradeServiceImpl.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        tradeServiceImpl.setTradeDAO(getTradeDAO());
        tradeServiceImpl.setFileService(getFileService());
        tradeServiceImpl.setEmailTradeService(new MockEmailTradeService());
        tradeServiceImpl.setAuthService(getAuthService());
        tradeServiceImpl.setUserService(getUserService());

        tradeServiceImpl.setLogger();
        return tradeServiceImpl;
    }

    private static UserServiceImpl getUserService(){
        if(userServiceImpl != null) return userServiceImpl;
        userServiceImpl = new UserServiceImpl();
        userServiceImpl.setAccountDAO(getAccountDAO());
        userServiceImpl.setEmailRecoveryService(new MockEmailRecoveryService());
        userServiceImpl.setEmailVerificationService(new MockEmailVerificationService());
        userServiceImpl.setUserDAO(getUserDAO());
        userServiceImpl.setLogger();
        return userServiceImpl;
    }

    private static AuthServiceImpl getAuthService(){
        if(authServiceImpl != null) return authServiceImpl;
        authServiceImpl = new AuthServiceImpl();
        authServiceImpl.setAccountDAO(accountDAO);
        authServiceImpl.setUserDAO(userDAO);
        return authServiceImpl;
    }

    public static FileServiceImpl getFileService(){
        FileServiceImpl fileService = new FileServiceImpl();
        fileService.setLogger();
        return fileService;
    }

    public static EmailTradeServiceImpl getEmailTradeService(){
        return new EmailTradeServiceImpl();
    }
}
