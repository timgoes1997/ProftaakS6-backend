package com.github.fontys.trackingsystem.dao;

import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.trackingsystem.dao.interfaces.BorderLocationDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BorderLocationDAOImpl implements BorderLocationDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(BorderLocation location) {
        em.persist(location);
    }

    @Override
    public void edit(BorderLocation location) {
        em.merge(location);
    }

    @Override
    public void remove(BorderLocation location) {
        em.remove(location);
    }

    @Override
    public BorderLocation findByID(long id) {
        return em.find(BorderLocation.class, id);
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
