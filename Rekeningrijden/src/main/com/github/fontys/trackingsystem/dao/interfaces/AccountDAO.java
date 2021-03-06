package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.user.Account;

public interface AccountDAO {
    void create(Account account);

    void edit(Account account);

    void remove(Account account);

    Account find(long id);

    Account findByUsername(String username);

    Account findByEmail(String email);

    boolean recoveryLinkExists(String link);

    Account findByRecoveryLink(String recoveryLink);
}
