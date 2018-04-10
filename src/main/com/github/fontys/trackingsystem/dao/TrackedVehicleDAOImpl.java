package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class TrackedVehicleDAOImpl implements TrackedVehicleDAO {

    @PersistenceContext(name="Proftaak")
    EntityManager em;

    @Override
    public void create(TrackedVehicle trackedVehicle) {
        em.persist(trackedVehicle);
    }

    @Override
    public void edit(TrackedVehicle trackedVehicle) {
        em.merge(trackedVehicle);
    }

    @Override
    public void remove(TrackedVehicle trackedVehicle) {
        em.remove(trackedVehicle);
    }

    @Override
    public TrackedVehicle findByID(long id) {
        return em.find(TrackedVehicle.class, id);
    }

    @Override
    public List<Location> findLocationsByTrackedVehicleID(long trackedVehicleID) {
        TypedQuery<Location> query =
                em.createNamedQuery(TrackedVehicle.FIND_LOCATIONSBYTRACKEDVEHICLEID, Location.class);
        query.setParameter("id", trackedVehicleID);
        return query.getResultList();
    }

    @Override
    public List<Location> findLocationsByCustomerVehicleID(long customerVehicleID) {
        TypedQuery<Location> query =
                em.createNamedQuery(TrackedVehicle.FIND_LOCATIONSBYCUSTOMERVEHICLEID, Location.class);
        query.setParameter("id", customerVehicleID);
        return query.getResultList();    }
}
