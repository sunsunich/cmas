package org.cmas.entities;


import com.google.myjson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable, HasId {

    private static final long serialVersionUID = -5985814100072616561L;

    //set by server
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    protected long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserBalance userBalance;

    //end set by server

    //set by user from mobile
    @Column(unique = true, nullable = false)
    protected String username;

    protected String password;

    protected String mobileLockCode;

    protected String email;

    //end set by user from mobile

    //used only on mob device
    @Expose
    @Transient
    protected long userTypeId;
    //end used only on mob device

    public User() {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(long userTypeId) {
        this.userTypeId = userTypeId;
    }

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

    public String getMobileLockCode() {
        return mobileLockCode;
    }

    public void setMobileLockCode(String mobileLockCode) {
        this.mobileLockCode = mobileLockCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserBalance getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(UserBalance userBalance) {
        this.userBalance = userBalance;
    }
}
