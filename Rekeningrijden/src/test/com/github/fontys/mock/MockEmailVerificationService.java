package com.github.fontys.mock;

import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.email.interfaces.EmailVerificationService;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;

import java.util.UUID;

public class MockEmailVerificationService implements EmailVerificationService {

    private UserDAO userDAO;

    @Override
    public String generateVerificationLink(User user) {
        String token = UUID.randomUUID().toString();
        if(userDAO.verificationLinkExists(token)){
            return generateVerificationLink(user);
        }else{
            return token;
        }
    }

    @Override
    public void sendVerificationMail(Account acc) {

    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
