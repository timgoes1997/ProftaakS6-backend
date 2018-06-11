package com.github.fontys.trackingsystem.services.email.interfaces;

import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;

public interface EmailVerificationService {
    String generateVerificationLink(User user);
    void sendVerificationMail(Account acc);
}
