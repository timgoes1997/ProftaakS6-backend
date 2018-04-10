package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class VehicleDAOImpl implements VehicleDAO{

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(Vehicle vehicle) {
        em.persist(vehicle);
    }

    @Override
    public void edit(Vehicle vehicle) {
        em.merge(vehicle);
    }

    @Override
    public void remove(Vehicle vehicle) {
        em.remove(vehicle);
    }

    @Override
    public Vehicle find(long id) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYID, Vehicle.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Vehicle find(String brand, Long vehicleModel, Date buildDate) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYMODELBRANDBUILDDATE, Vehicle.class);
        query.setParameter("id", vehicleModel);
        query.setParameter("brand", brand);
        query.setParameter("buildDate", buildDate);
        return query.getSingleResult();
    }

    @Override
    public List<Vehicle> findAll() {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_ALL, Vehicle.class);
        return query.getResultList();
    }

    @Override
    public List<Vehicle> findByModel(long id) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYMODEL, Vehicle.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Vehicle> findByBrand(String brand) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYBRAND, Vehicle.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    @Override
    public List<String> getBrands() {
        Query query = em.createQuery("SELECT DISTINCT v.brand FROM VEHICLE v");
        return query.getResultList();
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
