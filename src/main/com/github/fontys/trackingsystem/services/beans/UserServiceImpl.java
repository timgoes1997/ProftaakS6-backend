package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.email.EmailRecoveryService;
import com.github.fontys.trackingsystem.services.email.EmailVerificationService;
import com.github.fontys.trackingsystem.services.interfaces.UserService;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import java.util.logging.Logger;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private EmailVerificationService emailVerificationService;

    @Inject
    private EmailRecoveryService emailRecoveryService;

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
            if(userAccount == null){
                throw new InternalServerErrorException("Server had a problem while creating a user account");
            }
            createdUser.setVerifyLink(emailVerificationService.generateVerificationLink(createdUser));
            userDAO.edit(createdUser);
            emailVerificationService.sendVerificationMail(userAccount);
            return createdUser;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create customer: " + e.toString());
        }
    }

    @Override
    public boolean recoverPassword(String email) {
        Account acc = accountDAO.findByEmail(email);
        if (acc == null) {
            throw new ForbiddenException("User for given email doesn't exist");
        }

        try {
            acc.setRecoveryLink(emailRecoveryService.generateRecoveryLink(acc));
            accountDAO.edit(acc);
            emailRecoveryService.sendRecoveryMail(acc);
            return true;
        }catch (Exception e){
            throw new InternalServerErrorException("Couldn't send recovery mail, please contact a administrator");
        }
    }

    @Override
    public boolean hasRecoveryLink(String email, String recoveryLink) {
        Account acc = accountDAO.findByEmail(email);
        if (acc == null) {
            throw new ForbiddenException("User for given email doesn't exist");
        }

        return acc.getRecoveryLink().equals(recoveryLink);
    }

    @Override
    public User resetPassword(String email, String newPassword, String recoveryLink) {
        Account acc = accountDAO.findByEmail(email);
        if (acc == null && acc.getRecoveryLink().equals(recoveryLink)) {
            throw new ForbiddenException("User or recovery link doesn't exist");
        }

        acc.setPassword(newPassword);
        acc.setRecoveryLink(null);
        accountDAO.edit(acc);
        return acc.getUser();
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setEmailVerificationService(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    public void setEmailRecoveryService(EmailRecoveryService emailRecoveryService) {
        this.emailRecoveryService = emailRecoveryService;
    }

    public void setLogger() {
        this.logger = Logger.getLogger(UserServiceImpl.class.getName());
    }
}
