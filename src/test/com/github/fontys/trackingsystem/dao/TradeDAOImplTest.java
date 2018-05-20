package com.github.fontys.trackingsystem.dao;

import com.github.fontys.helper.PersistenceHelper;
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
        
    }
}