package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.auth.ESAuth;
import com.github.fontys.entities.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.interfaces.AuthService;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

public class AuthServiceImpl implements AuthService {

    @Inject
    @CurrentESUser
    private ESUser user;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @Override
    public Account logon(String email, String password, HttpServletRequest req) {
        try {
            Account a = accountDAO.findByEmail(email);
            if (a != null) {
                if(!a.getUser().getVerified()){
                    throw new NotAuthorizedException("User is not verified");
                }
                // do stuff with password
                if (a.getPassword().equals(password)) {

                    User u = a.getUser();
                    ESAuth.logon(req, u);
                    // set the last seen
                    u.setLastSeen();
                    // save
                    userDAO.edit(u);
                    return a;
                }
            }
        }catch (EJBException e) {
            throw new NotFoundException("User entered a invalid email or password");
        }
        throw new NotFoundException("User entered a invalid email or password");
    }

    @Override
    public void isLoggedIn() {
        if (user != null) {
            return;
        }
        throw new NotAuthorizedException("User is not authenticated");
    }

    @Override
    public void logout(HttpServletRequest req) {
        if (!ESAuth.logout(req)) {
            throw new InternalServerErrorException("Server failed to destroy user session");
        }
    }

    //test methods
    public void setUser(ESUser user) {
        this.user = user;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
