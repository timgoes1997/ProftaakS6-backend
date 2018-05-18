package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;

public interface UserService {
    Account getAccount(int id);
    Account getAccountByUsername(String name);
    Account getAccountByEmail(String email);
    User getUser(int id);
    User confirmRegistration(String token);
    User createCustomer(String name, String address, String residency, String email, String username, String password);
    boolean recoverPassword(String email);
    boolean hasRecoveryLink(String email, String recoveryLink);
    User resetPassword(String email, String newPassword, String recoveryLink);
}