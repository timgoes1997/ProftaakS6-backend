package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.VehicleModel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;
import java.util.List;

@Stateless
public class VehicleModelDAOImpl implements VehicleModelDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(VehicleModel vehicleModel) {
        em.persist(vehicleModel);
    }

    @Override
    public void edit(VehicleModel vehicleModel) { em.merge(vehicleModel); }

    @Override
    public void remove(VehicleModel vehicleModel) { em.remove(vehicleModel); }

    @Override
    public VehicleModel find(long id) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYID, VehicleModel.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public VehicleModel find(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel){
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL, VehicleModel.class);
        query.setParameter("modelName", modelName);
        query.setParameter("edition", edition);
        query.setParameter("fuelType", fuelType);
        query.setParameter("energyLabel", energyLabel);
        return query.getSingleResult();
    }

    @Override
    public List<VehicleModel> findAllModels() {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_ALL, VehicleModel.class);
        List<VehicleModel> results = query.getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByModelName(String modelName) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYNAME, VehicleModel.class);
        List<VehicleModel> results = query.setParameter("modelName", modelName).getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByEdition(String edition) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYEDITION, VehicleModel.class);
        List<VehicleModel> results = query.setParameter("edition", edition).getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByNameAndEdition(String modelName, String edition) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYNAMEANDEDITION, VehicleModel.class);
        query.setParameter("modelName", modelName);
        query.setParameter("edition", edition);
        List<VehicleModel> results = query.getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByFuelType(FuelType fuelType) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYFUELTYPE, VehicleModel.class);
        List<VehicleModel> results = query.setParameter("fuelType", fuelType).getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByEnergyLabel(EnergyLabel energyLabel) {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery(VehicleModel.FIND_BYENERGYLABEL, VehicleModel.class);
        List<VehicleModel> results = query.setParameter("energyLabel", energyLabel).getResultList();
        return results;
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
