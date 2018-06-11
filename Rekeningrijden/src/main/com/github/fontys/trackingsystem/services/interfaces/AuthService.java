package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.user.Account;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public interface AuthService {
    Account logon(String email, String password, HttpServletRequest req);
    void isLoggedIn();
    void logout(HttpServletRequest req);
}