package com.github.fontys.trackingsystem.dao;

import com.github.fontys.entities.region.Region;
import com.github.fontys.trackingsystem.dao.interfaces.RegionDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class RegionDAOImpl implements RegionDAO {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Region region) {
        em.persist(region);
    }

    @Override
    public void edit(Region region) {
        em.merge(region);
    }

    @Override
    public void remove(Region region) {
        em.remove(region);
    }

    @Override
    public boolean exists(String name) {
        TypedQuery<Region> query =
                em.createNamedQuery(Region.FIND_NAME, Region.class);
        return query.setParameter("name", name).getResultList().size() > 0;
    }

    @Override
    public boolean exists(long id) {
        TypedQuery<Region> query =
                em.createNamedQuery(Region.FIND_ID, Region.class);
        return query.setParameter("id", id).getResultList().size() > 0;
    }

    @Override
    public Region find(long id) {
        TypedQuery<Region> query =
                em.createNamedQuery(Region.FIND_ID, Region.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public Region find(String name) {
        TypedQuery<Region> query =
                em.createNamedQuery(Region.FIND_ID, Region.class);
        return query.setParameter("name", name).getSingleResult();
    }

    @Override
    public List<Region> getAllRegions() {
        TypedQuery<Region> query =
                em.createNamedQuery(Region.FIND_ALL, Region.class);
        return query.getResultList();
    }

}
