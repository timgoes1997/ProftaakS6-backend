package com.github.fontys.trackingsystem.dao;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class TradeDAOImplTest {

    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create(){
        PersistenceHelper.generateDummyData();
        Account acc = PersistenceHelper.getAccountDAO().findByEmail("admin@Admin.com");
        User user = PersistenceHelper.getUserDAO().findByAccount(acc);

        //Transfer transfer = new Transfer(user, );
    }
}