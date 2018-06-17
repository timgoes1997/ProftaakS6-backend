package com.github.fontys.trackingsystem.dao;

import com.github.fontys.entities.payment.ForeignBill;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.entities.payment.Bill;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
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
    public boolean exists(long id) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYID, Bill.class);
        query.setParameter("id", id);
        return query.getResultList().size() > 0;
    }

    @Override
    public Bill find(long id) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYID, Bill.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public List<Bill> findByOwnerId(Long ownerId) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYOWNERID, Bill.class);
        query.setParameter("ownerid", ownerId);
        return query.getResultList();
    }

    @Override
    public List<Bill> findByStatus(String status) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYSTATUS, Bill.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Bill> findByVehicleId(long vehicleId) {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYVEHICLEID, Bill.class);
        query.setParameter("id", vehicleId);
        return query.getResultList();
    }

    @Override
    public List<Bill> getAll() {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_ALL, Bill.class);
        return query.getResultList();
    }

    @Override
    public List<ForeignBill> getAllForeignBill() {
        TypedQuery<ForeignBill> query =
                em.createNamedQuery(ForeignBill.FIND_BYTYPE, ForeignBill.class);
        return query.getResultList();
    }

    @Override
    public List<Bill> getAllCountryBill() {
        TypedQuery<Bill> query =
                em.createNamedQuery(Bill.FIND_BYTYPE, Bill.class);
        return query.getResultList();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
