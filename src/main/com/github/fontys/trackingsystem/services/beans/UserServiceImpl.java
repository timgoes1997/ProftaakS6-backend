package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.EmailService;
import com.github.fontys.trackingsystem.services.interfaces.UserService;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import java.util.logging.Logger;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private EmailService emailService;

    @Inject
    private Logger logger;

    @Override
    public Account getAccount(int id) {
        try {
            return accountDAO.find(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't find account by given username");
        }
    }

    @Override
    public Account getAccountByUsername(String name) {
        try {
            return accountDAO.findByUsername(name);
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't find account by given username");
        }
    }

    @Override
    public Account getAccountByEmail(String email) {
        try {
            return accountDAO.findByEmail(email);
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't find account by given email");
        }
    }

    @Override
    public User getUser(int id) {
        try {
            return userDAO.find(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't find user by given id");
        }
    }

    @Override
    public User confirmRegistration(String token) {
        try {
            if (!userDAO.verificationLinkExists(token)) {
                throw new NotAcceptableException("Verification link doesn't exist");
            }

            if (userDAO.hasBeenVerified(token)) {
                throw new NotAcceptableException("You have already verified your account");
            }

            User user = userDAO.findByVerificationLink(token);
            if (user == null) {
                throw new InternalServerErrorException("Couldn't find a user with the given verification link");
            }
            user.setVerified(true);
            userDAO.edit(user);

            return user;

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while trying to verify user!");
        }
    }

    @Override
    public User createCustomer(String name, String address, String residency, String email, String username, String password) {


        Account acc = accountDAO.findByEmail(email);
        if (acc != null) {
            throw new ForbiddenException("User already exists for given email");
        }

        User user = new User(name, address, residency, Role.CUSTOMER);
        Account account = new Account(email, username, password);
        account.setUser(user);

        try {
            accountDAO.create(account);
            Account userAccount = accountDAO.findByEmail(account.getEmail());
            User createdUser = userDAO.findByAccount(userAccount);
            createdUser.setVerifyLink(emailService.generateVerificationLink(createdUser));
            userDAO.edit(createdUser);
            emailService.sendVerificationMail(userAccount);
            return createdUser;
        } catch (Exception e) { //Expects a NoResultException but that is hidden in EJBException
            throw new InternalServerErrorException("Failed to create customer: " + e.toString());
        }
    }
}
