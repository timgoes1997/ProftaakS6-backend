package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.tracking.Location;
import com.github.fontys.helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

public class BillGenerationTest {

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
    public void createTransfer() {
       PersistenceHelper.generateDummyData();
       List<Location> locationList = PersistenceHelper.getDataGenerator().generateRuhrBerlin();


    }

}