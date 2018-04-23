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
                em.createNamedQuery(User.FIND_BYID, User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public User findByAccount(Account acc) {
        TypedQuery<User> query =
                em.createNamedQuery(User.FIND_BYACCOUNT, User.class);
        query.setParameter("id", acc.getId());
        return query.getSingleResult();
    }

    @Override
    public User findByVerificationLink(String link) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK, User.class);
        return query.setParameter("link", link).getSingleResult();
    }

    @Override
    public boolean verificationLinkExists(String link) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK, User.class);
        return query.setParameter("link", link).getResultList().size() > 0;
    }

    @Override
    public boolean hasBeenVerified(String link) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK_AND_VERIFICATION, User.class);
        return query.setParameter("link", link)
                .setParameter("verified", true)
                .getResultList().size() > 0;
    }

    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
