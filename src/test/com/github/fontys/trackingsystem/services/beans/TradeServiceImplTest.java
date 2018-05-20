package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

public class TradeServiceImplTest {

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
    public void createTransfer() {
        PersistenceHelper.generateDummyData();
        Account acc = PersistenceHelper.getAccountDAO().findByEmail(transferFrom);
        Account acc2 = PersistenceHelper.getAccountDAO().findByEmail(transferTo);
        User user = PersistenceHelper.getUserDAO().findByAccount(acc);
        User user2 = PersistenceHelper.getUserDAO().findByAccount(acc2);
        List<RegisteredVehicle> registeredVehicles = PersistenceHelper.getRegisteredVehicleDAO().findByUser(user.getId());
        assertTrue(registeredVehicles.size() > 0);
        RegisteredVehicle registeredVehicle = registeredVehicles.get(0);

        TradeServiceImpl tradeService = PersistenceHelper.getTradeService();
        tradeService.setCurrentUser(user);

        String licensePlate = registeredVehicle.getLicensePlate();
        PersistenceHelper.getEntityManager().getTransaction().begin();
        Transfer transfer = tradeService.createTransfer(registeredVehicle.getLicensePlate(),acc.getEmail());
        PersistenceHelper.getEntityManager().getTransaction().commit();

        assertNotNull(transfer);

    }
}