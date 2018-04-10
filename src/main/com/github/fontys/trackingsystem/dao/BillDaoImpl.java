package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@RequestScoped
public class BillDaoImpl implements BillDAO {

    @PersistenceContext(name = "Proftaak")
    private EntityManager em;

    @Override
    public void create(Bill bill) {
        em.persist(bill);
    }

    @Override
    public void edit(Bill bill) {
        em.merge(bill);
    }

    @Override
    public void remove(Bill bill) {
        em.remove(bill);
    }

    @Override
    public Bill find(int id) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYID, Bill.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Bill> findByOwnerId(int ownerId) {
        // TODO: 2-4-18 Wat bedoelen we hier met de owner? account of username id?
        throw new NotImplementedException();
    }

    @Override
    public List<Bill> findByStatus(String status) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYSTATUS, Bill.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Bill> findByVehicleId(int vehicleId) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYVEHICLEID, Bill.class);
        query.setParameter("vehicleId", vehicleId);
        return query.getResultList();
    }

    @Override
    public List<Bill> getAll() {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_ALL, Bill.class);
        return query.getResultList();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
