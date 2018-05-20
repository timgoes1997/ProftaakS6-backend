package com.github.fontys.trackingsystem.dao;

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

public class TradeDAOImplTest {

    private EntityManager em;

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
        Account acc = PersistenceHelper.getAccountDAO().findByEmail("email 9");
        User user = PersistenceHelper.getUserDAO().findByAccount(acc);
        List<RegisteredVehicle> registeredVehicles = PersistenceHelper.getRegisteredVehicleDAO().findByUser(user.getId());
        TradeService tradeService = PersistenceHelper.getTradeService();

        assertTrue(registeredVehicles.size() > 0);

        RegisteredVehicle registeredVehicle = registeredVehicles.get(0);

        Transfer transfer = new Transfer(user, registeredVehicle, tradeService.generateTradeToken());

        assertTrue(transfer.getTransferToken() != null);
    }
}