package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

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

    @Override
    public Vehicle find(String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel){
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL, Vehicle.class);
        query.setParameter("modelName", modelName);
        query.setParameter("edition", edition);
        query.setParameter("fuelType", fuelType);
        query.setParameter("energyLabel", energyLabel);
        return query.getSingleResult();
    }

    @Override
    public Vehicle find(String brand, Date date, String modelName, String edition, FuelType fuelType, EnergyLabel energyLabel) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYNAMEEDITIONFUELTYPEENERGYLABEL, Vehicle.class);
        query.setParameter("brand", brand);
        query.setParameter("buildDate", date);
        query.setParameter("modelName", modelName);
        query.setParameter("edition", edition);
        query.setParameter("fuelType", fuelType);
        query.setParameter("energyLabel", energyLabel);
        return query.getSingleResult();
    }

    @Override
    public List<String> findModelsByBrand(String brand) {
        Query query = em.createQuery("SELECT DISTINCT v.modelName FROM VEHICLE v WHERE v.brand = :brand");
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    @Override
    public List<String> findEditionsByModelAndBrand(String brand, String model) {
        Query query = em.createQuery("SELECT DISTINCT v.edition FROM VEHICLE v WHERE v.brand = :brand AND v.modelName = :modelName");
        query.setParameter("brand", brand);
        query.setParameter("modelName", model);
        return query.getResultList();
    }

    @Override
    public List<String> findFuelTypesByModelBrandAndEdition(String brand, String model, String edition) {
        Query query = em.createQuery("SELECT DISTINCT v.fuelType FROM VEHICLE v WHERE v.brand = :brand AND v.modelName = :modelName AND v.edition = :edition");
        query.setParameter("brand", brand);
        query.setParameter("modelName", model);
        query.setParameter("edition", edition);
        return query.getResultList();
    }

    @Override
    public List<String> findEnergyLabelsByModelBrandAndEdition(String brand, String model, String edition) {
        Query query = em.createQuery("SELECT DISTINCT v.energyLabel FROM VEHICLE v WHERE v.brand = :brand AND v.modelName = :modelName AND v.edition = :edition");
        query.setParameter("brand", brand);
        query.setParameter("modelName", model);
        query.setParameter("edition", edition);
        return query.getResultList();
    }

    @Override
    public List<String> findEnergyLabelsByModelBrandEditionAndFuelType(String brand, String model, String edition, FuelType fuelType) {
        Query query = em.createQuery("SELECT DISTINCT v.energyLabel FROM VEHICLE v WHERE v.brand = :brand AND v.modelName = :modelName AND v.edition = :edition AND v.fuelType = :fuelType");
        query.setParameter("brand", brand);
        query.setParameter("modelName", model);
        query.setParameter("edition", edition);
        query.setParameter("fuelType", fuelType);
        return query.getResultList();
    }

    @Override
    public List<Vehicle> findModelsByModelName(String modelName) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYNAME, Vehicle.class);
        List<Vehicle> results = query.setParameter("modelName", modelName).getResultList();
        return results;
    }

    @Override
    public List<Vehicle> findModelsByEdition(String edition) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYEDITION, Vehicle.class);
        List<Vehicle> results = query.setParameter("edition", edition).getResultList();
        return results;
    }

    @Override
    public List<Vehicle> findModelsByNameAndEdition(String modelName, String edition) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYNAMEANDEDITION, Vehicle.class);
        query.setParameter("modelName", modelName);
        query.setParameter("edition", edition);
        List<Vehicle> results = query.getResultList();
        return results;
    }

    @Override
    public List<Vehicle> findModelsByFuelType(FuelType fuelType) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYFUELTYPE, Vehicle.class);
        List<Vehicle> results = query.setParameter("fuelType", fuelType).getResultList();
        return results;
    }

    @Override
    public List<Vehicle> findModelsByEnergyLabel(EnergyLabel energyLabel) {
        TypedQuery<Vehicle> query =
                em.createNamedQuery(Vehicle.FIND_BYENERGYLABEL, Vehicle.class);
        List<Vehicle> results = query.setParameter("energyLabel", energyLabel).getResultList();
        return results;
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
