package com.github.fontys.trackingsystem.services;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.auth.ESAuth;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.services.interfaces.AuthServiceInterface;
import com.github.fontys.trackingsystem.user.Account;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;

public class AuthService implements AuthServiceInterface {

    @Inject
    @CurrentESUser
    private ESUser user;

    @Inject
    private AccountDAO accountDAO;

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
                    ESAuth.logon(req, a.getUser());
                    return a;
                }
            }
        }catch (Exception e) {
            throw new NotFoundException("User entered a invalid email or password");
        }
        throw new NotAuthorizedException("User entered a invalid email or password");
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
}
