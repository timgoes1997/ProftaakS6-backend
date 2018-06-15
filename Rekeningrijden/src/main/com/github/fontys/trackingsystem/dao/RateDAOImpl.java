package com.github.fontys.trackingsystem.dao;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.Region;
import com.github.fontys.trackingsystem.dao.interfaces.RateDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class RateDAOImpl implements RateDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Rate regionRate) {
        em.persist(regionRate);
    }

    @Override
    public void edit(Rate regionRate) {
        em.merge(regionRate);
    }

    @Override
    public void remove(Rate regionRate) {
        em.remove(regionRate);
    }

    @Override
    public boolean exists(long id) {
        TypedQuery<Rate> query =
                em.createNamedQuery(Rate.FIND_ID, Rate.class);
        return query.setParameter("id", id).getResultList().size() > 0;
    }

    @Override
    public Rate find(long id) {
        TypedQuery<Rate> query =
                em.createNamedQuery(Rate.FIND_ID, Rate.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Rate> findRates(Region region) {
        TypedQuery<Rate> query =
                em.createNamedQuery(Rate.FIND_BY_REGION, Rate.class);
        return query.setParameter("id", region.getId()).getResultList();
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
