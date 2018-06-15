package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;

public interface UserService {
    Account getAccount(int id);
    Account getAccountByUsername(String name);
    Account getAccountByEmail(String email);
    User getUser(int id);
    User confirmRegistration(String token);
    User createCustomer(String name, String address, String residency, String email, String username, String password);
    boolean recoverPassword(String email);
    User resetPassword(String newPassword, String recoveryLink);
    User edit(String email, String address, String residency, String department);
}
