package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.CustomerVehicleDAO;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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
        TypedQuery<CustomerVehicle> query =
                em.createNamedQuery("CustomerVehicle.findByID", CustomerVehicle.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public CustomerVehicle findByLicense(String license) {
        TypedQuery<CustomerVehicle> query =
                em.createNamedQuery("CustomerVehicle.findByLicense", CustomerVehicle.class);
        query.setParameter("licensePlate", license);
        return query.getSingleResult();
    }

    @Override
    public List<CustomerVehicle> findByUser(long id) {
        TypedQuery<CustomerVehicle> query =
                em.createNamedQuery("CustomerVehicle.findByUser", CustomerVehicle.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<CustomerVehicle> findByVehicle(long id) {
        TypedQuery<CustomerVehicle> query =
                em.createNamedQuery("CustomerVehicle.findByVehicle", CustomerVehicle.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<CustomerVehicle> getAll() {
        TypedQuery<CustomerVehicle> query =
                em.createNamedQuery("CustomerVehicle.findAll", CustomerVehicle.class);
        return query.getResultList();
    }
}
