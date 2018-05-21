package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.TrackedVehicleDAO;
import com.github.fontys.trackingsystem.tracking.Location;
import com.github.fontys.trackingsystem.tracking.TrackedVehicle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TrackedVehicleDAOImpl implements TrackedVehicleDAO {

    @PersistenceContext(name = "Proftaak")
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
    public TrackedVehicle findByRegisteredVehicleID(long id) {
        TypedQuery<TrackedVehicle> query = em.createNamedQuery(TrackedVehicle.FIND_TRACKEDVEHICLEBYCUSTOMERVEHICLEID, TrackedVehicle.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void addNewLocationByTrackedVehicle(long trackedVehicleID) {

    }

    @Override
    public List<Location> findLocationsByTrackedVehicleID(long trackedVehicleID) {
        TypedQuery<Location> query =
                em.createNamedQuery(TrackedVehicle.FIND_LOCATIONSBYTRACKEDVEHICLEID, Location.class);
        query.setParameter("id", trackedVehicleID);
        return query.getResultList();
    }

    @Override
    public List<Location> findLocationsByRegisteredVehicleID(long customerVehicleID) {
        TypedQuery<Location> query =
                em.createNamedQuery(TrackedVehicle.FIND_LOCATIONSBYREGISTEREDVEHICLEID, Location.class);
        query.setParameter("id", customerVehicleID);
        return query.getResultList();
    }

    @Override
    public Location findLastLocationByTrackedVehicleID(long trackedVehicleID) {
        TypedQuery<Location> query =
                em.createNamedQuery(TrackedVehicle.FIND_LASTLOCATIONBYTRACKEDVEHICLEID, Location.class);
        query.setParameter("id", trackedVehicleID);
        return query.getSingleResult();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
