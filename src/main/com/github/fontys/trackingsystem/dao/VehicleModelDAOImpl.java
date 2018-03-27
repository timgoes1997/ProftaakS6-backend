package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.VehicleModelDAO;
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
    public void remove(VehicleModel vehicleModel) {

    }

    @Override
    public VehicleModel find(long id) {
        return null;
    }

    @Override
    public List<VehicleModel> findAllModels() {
        TypedQuery<VehicleModel> query =
                em.createNamedQuery("VehicleModel.findAll", VehicleModel.class);
        List<VehicleModel> results = query.getResultList();
        return results;
    }

    @Override
    public List<VehicleModel> findModelsByModelName(String modelName) {
        return null;
    }

    @Override
    public List<VehicleModel> findModelsByEdition(String modelName) {
        return null;
    }

    @Override
    public List<VehicleModel> findModelsByFuelType(String modelName) {
        return null;
    }

    @Override
    public List<VehicleModel> findModelsByEnergyLabel(String modelName) {
        return null;
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
