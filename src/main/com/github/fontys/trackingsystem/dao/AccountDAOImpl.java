package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.user.Account;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.logging.Logger;

@Stateless
public class AccountDAOImpl implements AccountDAO {

    @PersistenceContext(name = "Proftaak")
    private EntityManager em;

    @Inject
    private Logger logger;

    @Override
    public void create(Account account) {
        logger.info("lekker bezig");
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
        try {
            TypedQuery<Account> query =
                    em.createNamedQuery(Account.FIND_BYID, Account.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Account findByUsername(String username) {
        try {
            TypedQuery<Account> query =
                    em.createNamedQuery(Account.FIND_BYUSERNAME, Account.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Account findByEmail(String email) {
        try {
            TypedQuery<Account> query =
                    em.createNamedQuery(Account.FIND_BYEMAIL, Account.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
