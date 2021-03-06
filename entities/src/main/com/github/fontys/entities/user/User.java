package com.github.fontys.entities.user;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.fontys.entities.security.base.ESUser;
import com.github.fontys.entities.vehicle.RegisteredVehicle;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity(name = "CUSTOMER")
@NamedQueries({
        @NamedQuery(name = User.FIND_BYID,
                query = "SELECT c FROM CUSTOMER c WHERE c.id=:id"),
        @NamedQuery(name = User.FIND_BYACCOUNT,
                query = "SELECT c FROM CUSTOMER c WHERE c.account.id=:id"),
        @NamedQuery(name = User.FIND_VERIFICATION_LINK,
                query = "SELECT c FROM CUSTOMER c WHERE c.verifyLink = :link"),
        @NamedQuery(name = User.FIND_VERIFICATION_LINK_AND_VERIFICATION,
                query = "SELECT c FROM CUSTOMER c WHERE c.verifyLink = :link AND c.verified = :verified"),
})

public class User implements Serializable, ESUser {

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_BYID = "Account.findByID";
    public static final String FIND_BYACCOUNT = "Account.findByAccount";
    public static final String FIND_VERIFICATION_LINK = "User.findVerificationLink";
    public static final String FIND_VERIFICATION_LINK_AND_VERIFICATION = "User.findVerificationLinkAndVerified";

    // ======================================
    // =             Fields              =
    // ======================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "VERIFIED", nullable = false)
    private Boolean verified;

    @Size(min = 0, max = 128)
    @Column(name = "VERIFICATION_LINK")
    private String verifyLink;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "RESIDENCY")
    private String residency;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Account account;

    @Temporal(TemporalType.DATE)
    @Column(name="LAST_LOGGED_IN")
    private Calendar lastSeen;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEPARTMENT")
    private Department department;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<RegisteredVehicle> registeredVehicles;

    public User(String name, String address, String residency, Role role) {
        this.name = name;
        this.address = address;
        this.residency = residency;
        this.role = role;
        this.verified = false;
    }

    public User() {
        this.verified = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    public String getAddress() {
        return address;
    }

    public void setLastSeen() {
        this.lastSeen = Calendar.getInstance();
    }

    @JsonGetter
    @JsonbProperty
    @XmlAttribute
    public boolean isAppUser() {
        return lastSeen != null;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public Role getRole() {
        return role;
    }

    @JsonGetter
    @JsonbProperty
    @XmlAttribute
    public String getEmail() {
        if (account == null)
        {
            return "";
        }
        return account.getEmail();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @JsonIgnore
    @JsonbTransient
    @XmlTransient
    public List<RegisteredVehicle> getRegisteredVehicles() {
        return registeredVehicles;
    }

    public void setRegisteredVehicles(List<RegisteredVehicle> registeredVehicles) {
        this.registeredVehicles = registeredVehicles;
    }

    @JsonGetter
    @JsonbProperty
    @XmlAttribute
    public long getId() {
        return id;
    }

    @Override
    public Role getPrivilege() {
        return getRole();
    }

    public Boolean getVerified() { return verified; }

    public String getVerifyLink() { return verifyLink; }

    public void setVerifyLink(String verifyLink) { this.verifyLink = verifyLink; }

    public void setVerified(Boolean verified) { this.verified = verified; }
}
