package com.github.fontys.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceHelper {
    private static final EntityManager entityManager;
    static {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProftaakTest");
        entityManager = emf.createEntityManager();
    }
    public static EntityManager getEntityManager() {
        return entityManager;
    };
}
