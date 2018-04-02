package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.user.Account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class AccountDAOImpl implements AccountDAO{

    @PersistenceContext(name="Proftaak")
    private EntityManager em;

    @Override
    public void create(Account account) {
        em.persist(account);
    }

    @Override
    public void edit(Account account) {
        em.merge(account);
    }

    @Override
    public void remove(Account account) {
        em.remove(account);
    }

    @Override
    public Account find(long id) {
        TypedQuery<Account> query =
                em.createNamedQuery("Account.findByID", Account.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Account findByUsername(String username) {
        TypedQuery<Account> query =
                em.createNamedQuery("Account.findByUsername", Account.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    @Override
    public Account findByEmail(String email) {
        TypedQuery<Account> query =
                em.createNamedQuery("Account.findByEmail", Account.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
