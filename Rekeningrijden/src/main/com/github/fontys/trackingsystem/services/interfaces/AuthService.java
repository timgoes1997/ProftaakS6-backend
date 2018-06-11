package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.user.Account;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    Account logon(String email, String password, HttpServletRequest req);
    void isLoggedIn();
    void logout(HttpServletRequest req);
}