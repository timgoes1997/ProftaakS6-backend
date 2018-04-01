package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.CustomerVehicleDAO;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CustomerVehicleDAOImpl implements CustomerVehicleDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(CustomerVehicle customerVehicle) {
        em.persist(customerVehicle);
    }

    @Override
    public void edit(CustomerVehicle customerVehicle) {
        em.merge(customerVehicle);
    }

    @Override
    public void remove(CustomerVehicle customerVehicle) {
        em.remove(customerVehicle);
    }

    @Override
    public CustomerVehicle find(long id) {
        return null;
    }

    @Override
    public CustomerVehicle findByUser(long id) {
        return null;
    }

    @Override
    public CustomerVehicle findByVehicle(long id) {
        return null;
    }
}
