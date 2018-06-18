package com.github.fontys.helper;

import com.github.fontys.mock.MockEmailRecoveryService;
import com.github.fontys.mock.MockEmailTradeService;
import com.github.fontys.mock.MockEmailVerificationService;
import com.github.fontys.trackingsystem.DummyDataGenerator;
import com.github.fontys.trackingsystem.dao.*;
import com.github.fontys.trackingsystem.dao.interfaces.*;
import com.github.fontys.trackingsystem.services.beans.*;
import com.github.fontys.trackingsystem.services.email.EmailTradeServiceImpl;
import com.github.fontys.trackingsystem.services.file.FileServiceImpl;
import com.github.fontys.trackingsystem.services.interfaces.AuthService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

public class PersistenceHelper {

    private static final Logger LOGGER = Logger.getLogger(PersistenceHelper.class.getName());

    //DAO
    private static AccountDAO accountDAO;
    private static BillDAO billDAO;
    private static LocationDAO locationDAO;
    private static RegisteredVehicleDAO registeredVehicleDAO;
    private static TrackedVehicleDAO trackedVehicleDAO;
    private static TradeDAO tradeDAO;
    private static UserDAO userDAO;
    private static VehicleDAO vehicleDAO;
    private static RegionDAO regionDAO;
    private static RateDAO rateDAO;
    private static BorderLocationDAO borderLocationDAO;


    private static DummyDataGenerator dataGenerator;
    //Services
    private static TradeServiceImpl tradeServiceImpl;
    private static AuthServiceImpl authServiceImpl;
    private static UserServiceImpl userServiceImpl;

    private static LocationServiceImpl locationService;
    private static BillServiceImpl billService;
    private static GenerationServiceImpl generationService;
    private static RegionServiceImpl regionService;
    private static RouteServiceImpl routeService;


    private static final EntityManager entityManager;

    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProftaakTest");
        entityManager = emf.createEntityManager();
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    ;

    public static void cleanDataBase() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM ACCOUNT").executeUpdate();
        entityManager.createNativeQuery("UPDATE ROUTE_DETAIL SET RATE_ID = null").executeUpdate();
        entityManager.createQuery("DELETE FROM BILL").executeUpdate();
        entityManager.createQuery("DELETE FROM ROUTE").executeUpdate();
        entityManager.createQuery("DELETE FROM ROUTE_DETAIL").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET REGISTERED_VEHICLE = null").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET LAST_LOCATION = null").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRACKED_VEHICLE SET HARDWARE = null").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRANSFER SET CURRENT_OWNER_ID = null").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRANSFER SET TRANSFER_OWNER_ID = null").executeUpdate();
        entityManager.createNativeQuery("UPDATE TRANSFER SET REGISTERED_VEHICLE_ID = null").executeUpdate();
        entityManager.createQuery("DELETE FROM REGISTERED_VEHICLE").executeUpdate();
        entityManager.createQuery("DELETE FROM TRACKED_VEHICLE").executeUpdate();
        //entityManager.createQuery("DELETE FROM TRACKEDVEHICLE_LOCATIONS");
        entityManager.createQuery("DELETE FROM RATE").executeUpdate();
        entityManager.createQuery("DELETE FROM CUSTOMER").executeUpdate();
        entityManager.createQuery("DELETE FROM HARDWARE").executeUpdate();
        entityManager.createQuery("DELETE FROM VEHICLE").executeUpdate();
        entityManager.createQuery("DELETE FROM TRACKING_LOCATION").executeUpdate();
        entityManager.createQuery("DELETE FROM TRANSFER").executeUpdate();
        entityManager.createQuery("DELETE FROM REGION").executeUpdate();
        entityManager.getTransaction().commit();
    }

    public static void generateDummyData() {
        entityManager.getTransaction().begin();
        dataGenerator = new DummyDataGenerator();
        dataGenerator.setAccountDAO(getAccountDAO());
        dataGenerator.setBillDAO(getBillDAO());
        dataGenerator.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        dataGenerator.setUserDAO(getUserDAO());
        dataGenerator.setVehicleDAO(getVehicleDAO());
        dataGenerator.setTradeDAO(getTradeDAO());
        dataGenerator.setRegionDAO(getRegionDAO());
        dataGenerator.setRateDAO(getRateDAO());
        dataGenerator.setEm(entityManager);
        dataGenerator.init();
        entityManager.getTransaction().commit();
    }

    public static DummyDataGenerator getDataGenerator() {
        return dataGenerator;
    }

    public static AccountDAO getAccountDAO() {
        if (accountDAO != null) return accountDAO;
        AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
        accountDAOImpl.setEntityManager(entityManager);
        accountDAO = accountDAOImpl;
        return accountDAO;
    }

    public static BorderLocationDAO getBorderLocationDAO(){
        if(borderLocationDAO != null) return borderLocationDAO;
        BorderLocationDAOImpl borderLocationDAOImpl = new BorderLocationDAOImpl();
        borderLocationDAOImpl.setEntityManager(entityManager);
        borderLocationDAO = borderLocationDAOImpl;
        return borderLocationDAO;
    }

    public static BillDAO getBillDAO() {
        if (billDAO != null) return billDAO;
        BillDaoImpl billDAOImpl = new BillDaoImpl();
        billDAOImpl.setEntityManager(entityManager);
        billDAO = billDAOImpl;
        return billDAO;
    }

    public static RateDAO getRateDAO() {
        if (rateDAO != null) return rateDAO;
        RateDAOImpl rateDAOImpl = new RateDAOImpl();
        rateDAOImpl.setEntityManager(entityManager);
        rateDAO = rateDAOImpl;
        return rateDAO;
    }

    public static RegionDAO getRegionDAO() {
        if (regionDAO != null) return regionDAO;
        RegionDAOImpl regionDAOImpl = new RegionDAOImpl();
        regionDAOImpl.setEntityManager(entityManager);
        regionDAO = regionDAOImpl;
        return regionDAO;
    }

    public static LocationDAO getLocationDAO() {
        if (locationDAO != null) return locationDAO;
        LocationDAOImpl locationDAOImpl = new LocationDAOImpl();
        locationDAOImpl.setEntityManager(entityManager);
        locationDAO = locationDAOImpl;
        return locationDAO;
    }

    public static RegisteredVehicleDAO getRegisteredVehicleDAO() {
        if (registeredVehicleDAO != null) return registeredVehicleDAO;
        RegisteredVehicleDAOImpl registeredVehicleDAOImpl = new RegisteredVehicleDAOImpl();
        registeredVehicleDAOImpl.setEntityManager(entityManager);
        registeredVehicleDAO = registeredVehicleDAOImpl;
        return registeredVehicleDAO;
    }

    public static TrackedVehicleDAO getTrackedVehicleDAO() {
        if (trackedVehicleDAO != null) return trackedVehicleDAO;
        TrackedVehicleDAOImpl trackedVehicleDAOImpl = new TrackedVehicleDAOImpl();
        trackedVehicleDAOImpl.setEntityManager(entityManager);
        trackedVehicleDAO = trackedVehicleDAOImpl;
        return trackedVehicleDAO;
    }

    public static TradeDAO getTradeDAO() {
        if (tradeDAO != null) return tradeDAO;
        TradeDAOImpl tradeDAOImpl = new TradeDAOImpl();
        tradeDAOImpl.setEntityManager(entityManager);
        tradeDAO = tradeDAOImpl;
        return tradeDAO;
    }

    public static UserDAO getUserDAO() {
        if (userDAO != null) return userDAO;
        UserDAOImpl userDAOImpl = new UserDAOImpl();
        userDAOImpl.setEntityManager(entityManager);
        userDAO = userDAOImpl;
        return userDAO;
    }

    public static VehicleDAO getVehicleDAO() {
        if (vehicleDAO != null) return vehicleDAO;
        VehicleDAOImpl vehicleDAOImpl = new VehicleDAOImpl();
        vehicleDAOImpl.setEntityManager(entityManager);
        vehicleDAO = vehicleDAOImpl;
        return vehicleDAO;
    }

    public static TradeServiceImpl getTradeService() {
        if (tradeServiceImpl != null) return tradeServiceImpl;
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

    public static UserServiceImpl getUserService() {
        if (userServiceImpl != null) return userServiceImpl;
        userServiceImpl = new UserServiceImpl();
        userServiceImpl.setAccountDAO(getAccountDAO());
        userServiceImpl.setEmailRecoveryService(new MockEmailRecoveryService());
        userServiceImpl.setEmailVerificationService(new MockEmailVerificationService());
        userServiceImpl.setUserDAO(getUserDAO());
        userServiceImpl.setLogger();
        return userServiceImpl;
    }

    public static AuthServiceImpl getAuthService() {
        if (authServiceImpl != null) return authServiceImpl;
        authServiceImpl = new AuthServiceImpl();
        authServiceImpl.setAccountDAO(getAccountDAO());
        authServiceImpl.setUserDAO(getUserDAO());
        return authServiceImpl;
    }

    public static BillServiceImpl getBillService() {
        if(billService != null) return billService;
        billService = new BillServiceImpl();
        billService.setAccountDAO(getAccountDAO());
        billService.setBillDAO(getBillDAO());
        billService.setVehicleDAO(getVehicleDAO());
        billService.setLocationService(getLocationService());
        billService.setRegionService(getRegionService());
        return billService;

    }

    public static RegionServiceImpl getRegionService() {
        if (regionService != null) return regionService;
        regionService = new RegionServiceImpl();
        regionService.setLogger(LOGGER);
        regionService.setRateDAO(getRateDAO());
        regionService.setRegionDAO(getRegionDAO());
        regionService.setUserDAO(getUserDAO());
        regionService.setBorderLocationDAO(getBorderLocationDAO());
        return regionService;
    }

    public static RouteServiceImpl getRouteService(){
        if(routeService != null) return routeService;
        routeService = new RouteServiceImpl();
        routeService.setRegionService(getRegionService());
        routeService.setGenerationService(getGenerationService());
        return routeService;
    }

    public static GenerationServiceImpl getGenerationService() {
        if(generationService != null) return generationService;
        generationService = new GenerationServiceImpl();
        generationService.setBillDAO(getBillDAO());
        generationService.setBillService(getBillService());
        generationService.setLocationService(getLocationService());
        generationService.setRegionService(getRegionService());
        generationService.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        generationService.setRouteService(getRouteService());
        generationService.setLogger(LOGGER);
        return generationService;
    }

    public static LocationServiceImpl getLocationService() {
        if (locationService != null) return locationService;
        locationService = new LocationServiceImpl();
        locationService.setRegisteredVehicleDAO(getRegisteredVehicleDAO());
        locationService.setTrackedVehicleDAO(getTrackedVehicleDAO());
        return locationService;
    }


    public static FileServiceImpl getFileService() {
        FileServiceImpl fileService = new FileServiceImpl();
        fileService.setLogger();
        return fileService;
    }

    public static EmailTradeServiceImpl getEmailTradeService() {
        return new EmailTradeServiceImpl();
    }
}
