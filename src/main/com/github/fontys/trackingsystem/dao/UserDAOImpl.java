package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UserDAOImpl implements UserDAO{

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void edit(User user) {
        em.merge(user);
    }

    @Override
    public void remove(User user) {
        em.remove(user);
    }

    @Override
    public User find(long id) {
        TypedQuery<User> query =
                em.createNamedQuery("Customer.findByID", User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public User findByAccount(Account acc) {
        TypedQuery<User> query =
                em.createNamedQuery("Customer.findByAccount", User.class);
        query.setParameter("id", acc.getId());
        return query.getSingleResult();
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}