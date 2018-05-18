package com.github.fontys.trackingsystem.user;

import com.sun.mail.imap.ACL;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Calendar;

@Entity(name = "ACCOUNT")
@NamedQueries({
        @NamedQuery(name = Account.FIND_BYID,
                query = "SELECT a FROM ACCOUNT a WHERE a.id=:id"),
        @NamedQuery(name = Account.FIND_BYUSERNAME,
                query = "SELECT a FROM ACCOUNT a WHERE a.username=:username"),
        @NamedQuery(name = Account.FIND_BYEMAIL,
                query = "SELECT a FROM ACCOUNT a WHERE a.email=:email"),
        @NamedQuery(name = Account.FIND_RECOVERY_LINK,
                query = "SELECT a FROM ACCOUNT a WHERE a.recoveryLink=:link"),
})
public class Account {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_BYID = "User.findById";
    public static final String FIND_BYUSERNAME = "User.findByUsername";
    public static final String FIND_BYEMAIL = "User.findByEmail";
    public static final String FIND_RECOVERY_LINK = "User.findRecoveryLink";

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name="EMAIL", unique = true)
    private String email;

    @Column(name="USERNAME", unique = true)
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="RECOVERY_LINK")
    private String recoveryLink;

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

    public String getRecoveryLink() {
        return recoveryLink;
    }

    public void setRecoveryLink(String recoveryLink) {
        this.recoveryLink = recoveryLink;
    }
}
