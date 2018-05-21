package com.github.fontys.mock;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.services.email.interfaces.EmailRecoveryService;
import com.github.fontys.trackingsystem.user.Account;

import java.util.UUID;

public class MockEmailRecoveryService implements EmailRecoveryService {

    private AccountDAO accountDAO;

    @Override
    public String generateRecoveryLink(Account account) {
        String token = UUID.randomUUID().toString();
        if(accountDAO.recoveryLinkExists(token)){
            return generateRecoveryLink(account);
        }else{
            return token;
        }
    }

    @Override
    public void sendRecoveryMail(Account acc) {

    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
