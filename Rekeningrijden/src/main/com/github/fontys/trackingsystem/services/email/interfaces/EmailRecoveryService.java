package com.github.fontys.trackingsystem.services.email.interfaces;

import com.github.fontys.trackingsystem.user.Account;

public interface EmailRecoveryService {
    String generateRecoveryLink(Account account);
    void sendRecoveryMail(Account acc);

}
