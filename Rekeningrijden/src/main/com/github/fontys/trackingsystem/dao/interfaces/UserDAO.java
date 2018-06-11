package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;

public interface UserDAO {
    void create(User user);

    void edit(User user);

    void remove(User user);

    User find(long id);

    User findByAccount(Account acc);

    User findByVerificationLink(String link);

    boolean verificationLinkExists(String link);

    boolean hasBeenVerified(String link);
}
