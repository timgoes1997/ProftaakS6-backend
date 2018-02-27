package com.github.fontys.user;

public class Account {
    private String email;
    private String username;
    private String password;
    private User user;

    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
