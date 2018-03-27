package com.github.fontys.trackingsystem.user;

import com.sun.mail.imap.ACL;

import javax.persistence.*;

@Entity(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="EMAIL")
    private String email;

    @Column(name="USERNAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    public Account(String email, String username, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }
}
