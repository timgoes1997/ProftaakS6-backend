package com.github.fontys.trackingsystem.user;

import com.sun.mail.imap.ACL;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name = "ACCOUNT")
@NamedQueries({
        @NamedQuery(name = "Account.findByID",
                query = "SELECT a FROM ACCOUNT a WHERE a.id=:id"),
        @NamedQuery(name = "Account.findByUsername",
                query = "SELECT a FROM ACCOUNT a WHERE a.username=:username"),
        @NamedQuery(name = "Account.findByEmail",
                query = "SELECT a FROM ACCOUNT a WHERE a.email=:email"),
})
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

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="USER_ID")
    private User user;

    public Account(String email, String username, String password) {
        this.username = username;
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

    @XmlTransient
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
