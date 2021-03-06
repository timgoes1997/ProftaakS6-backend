package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.LocationDAO;
import com.github.fontys.entities.tracking.Location;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LocationDAOImpl implements LocationDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(Location location) {
        em.persist(location);
    }

    @Override
    public void edit(Location location) {
        em.merge(location);
    }

    @Override
    public void remove(Location location) {
        em.remove(location);
    }

    @Override
    public Location findByID(long id) {
        return em.find(Location.class, id);
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
