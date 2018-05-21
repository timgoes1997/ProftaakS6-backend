package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RegisteredVehicleDAOImpl implements RegisteredVehicleDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(RegisteredVehicle registeredVehicle) {
        registeredVehicle.setLicensePlate(registeredVehicle.getLicensePlate().toUpperCase());
        em.persist(registeredVehicle);
    }

    @Override
    public void edit(RegisteredVehicle registeredVehicle) {
        em.merge(registeredVehicle);
    }

    @Override
    public void remove(RegisteredVehicle registeredVehicle) {
        em.remove(registeredVehicle);
    }

    @Override
    public RegisteredVehicle find(long id) {
        TypedQuery<RegisteredVehicle> query =
                em.createNamedQuery(RegisteredVehicle.FIND_BYID, RegisteredVehicle.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public RegisteredVehicle findByLicense(String license) {
        TypedQuery<RegisteredVehicle> query =
                em.createNamedQuery(RegisteredVehicle.FIND_BYLICENSEPLATE, RegisteredVehicle.class);
        query.setParameter("licensePlate", license);
        return query.getSingleResult();
    }

    @Override
    public List<RegisteredVehicle> findByUser(long id) {
        TypedQuery<RegisteredVehicle> query =
                em.createNamedQuery(RegisteredVehicle.FIND_BYUSER, RegisteredVehicle.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public RegisteredVehicle findByVehicle(long id) {
        TypedQuery<RegisteredVehicle> query =
                em.createNamedQuery(RegisteredVehicle.FIND_BYVEHICLE, RegisteredVehicle.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<RegisteredVehicle> getAll() {
        TypedQuery<RegisteredVehicle> query =
                em.createNamedQuery(RegisteredVehicle.FIND_ALL, RegisteredVehicle.class);
        return query.getResultList();
    }
}
