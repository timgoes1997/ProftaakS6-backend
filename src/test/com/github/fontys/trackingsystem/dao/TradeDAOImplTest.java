package com.github.fontys.trackingsystem.dao;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.transfer.TransferStatus;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

public class TradeDAOImplTest {

    private EntityManager em;

    private String transferFrom = "email 9";
    private String transferTo = "email 1";

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        PersistenceHelper.cleanDataBase();
    }

    @Test
    public void create(){
        PersistenceHelper.generateDummyData();
        Account acc = PersistenceHelper.getAccountDAO().findByEmail(transferFrom);
        User user = PersistenceHelper.getUserDAO().findByAccount(acc);
        List<RegisteredVehicle> registeredVehicles = PersistenceHelper.getRegisteredVehicleDAO().findByUser(user.getId());
        TradeService tradeService = PersistenceHelper.getTradeService();

        assertTrue(registeredVehicles.size() > 0);
        RegisteredVehicle registeredVehicle = registeredVehicles.get(0);

        Transfer transfer = new Transfer(user, registeredVehicle, tradeService.generateTradeToken());
        PersistenceHelper.getEntityManager().getTransaction().begin();
        PersistenceHelper.getTradeDAO().create(transfer);
        PersistenceHelper.getEntityManager().getTransaction().commit();

        Transfer test = PersistenceHelper.getTradeDAO().findByToken(transfer.getTransferToken());
        assertTrue(test.getId() != null);
        assertTrue(test.getId() >= 1);

        assertTrue(transfer.getTransferToken() != null);
    }

    @Test
    public void acceptTransfer(){
        PersistenceHelper.generateDummyData();
        Account acc = PersistenceHelper.getAccountDAO().findByEmail(transferFrom);
        Account acc2 = PersistenceHelper.getAccountDAO().findByEmail(transferTo);
        User user = PersistenceHelper.getUserDAO().findByAccount(acc);
        User user2 = PersistenceHelper.getUserDAO().findByAccount(acc2);
        List<RegisteredVehicle> registeredVehicles = PersistenceHelper.getRegisteredVehicleDAO().findByUser(user.getId());
        TradeService tradeService = PersistenceHelper.getTradeService();

        assertTrue(registeredVehicles.size() > 0);
        RegisteredVehicle registeredVehicle = registeredVehicles.get(0);

        Transfer transfer = new Transfer(user, registeredVehicle, tradeService.generateTradeToken());
        PersistenceHelper.getEntityManager().getTransaction().begin();
        PersistenceHelper.getTradeDAO().create(transfer);
        PersistenceHelper.getEntityManager().getTransaction().commit();

        Transfer test = PersistenceHelper.getTradeDAO().findByToken(transfer.getTransferToken());
        assertTrue(test.getId() != null);
        assertTrue(test.getId() >= 1);

        test.setOwnerToTransferTo(user2);
        PersistenceHelper.getEntityManager().getTransaction().begin();
        PersistenceHelper.getTradeDAO().edit(test);
        PersistenceHelper.getEntityManager().getTransaction().commit();
        assertEquals(test.getOwnerToTransferTo(), user2);

        PersistenceHelper.getEntityManager().getTransaction().begin();
        test.acceptedNewOwner();
        PersistenceHelper.getEntityManager().getTransaction().commit();

        assertEquals(test.getStatus(), TransferStatus.AcceptedNewOwner);
    }
}