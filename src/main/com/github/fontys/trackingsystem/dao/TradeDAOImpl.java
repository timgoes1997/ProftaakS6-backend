package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.TradeDAO;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class TradeDAOImpl implements TradeDAO {

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(Transfer transfer) {
        em.persist(transfer);
    }

    @Override
    public void edit(Transfer transfer) {
        em.merge(transfer);
    }

    @Override
    public void remove(Transfer transfer) {
        em.remove(transfer);
    }

    @Override
    public boolean tokenExists(String token) {
        TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BY_TOKEN, Transfer.class);
        return query.setParameter("token", token).getResultList().size() > 0;
    }

    @Override
    public Transfer find(long id) {
        try {
            TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BYID, Transfer.class);
            return query.setParameter("id", id).getSingleResult();
        } catch(Exception e){
            return null;
        }
    }

    @Override
    public Transfer findByToken(String token) {
        try {
            TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BY_TOKEN, Transfer.class);
            return query.setParameter("token", token).getSingleResult();
        } catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Transfer> findFromUser(User user) {
        try {
            TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BY_CURRENT_OWNER, Transfer.class);
            return query.setParameter("id", user.getId()).getResultList();
        } catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Transfer> findToUser(User user) {
        try {
            TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BY_NEW_OWNER, Transfer.class);
            return query.setParameter("id", user.getId()).getResultList();
        } catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Transfer> findForVehicle(RegisteredVehicle vehicle) {
        try {
            TypedQuery<Transfer> query = em.createNamedQuery(Transfer.FIND_BY_VEHICLE, Transfer.class);
            return query.setParameter("id", vehicle.getId()).getResultList();
        } catch(Exception e){
            return null;
        }
    }
}
