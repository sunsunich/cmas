package org.cmas.entities;


import com.google.myjson.annotations.Expose;
import org.cmas.Globals;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings({"CallToSimpleGetterFromWithinClass", "InstanceofInterfaces"})
@MappedSuperclass
public abstract class User implements Serializable, HasId {

    private static final long serialVersionUID = -5985814100072616561L;

    //set by server
    @Id
    @Expose
    protected long id;

    @OneToOne
    protected UserBalance userBalance;

    @Column
    protected Date dateReg;

    @Column
    protected boolean enabled;

    @Column
    protected Date lastAction;

    @Column
    @Enumerated(EnumType.STRING)
    protected Role role;

    //registration locale
    @Column(nullable = false)
    protected Locale locale;

    @Column
    protected String lostPasswdCode;

    protected String newMail;

    @Column(length = 32)
    protected String md5newMail;

    //end set by server

    //set by user from mobile

    protected String password;

    protected String generatedPassword;

    protected String mobileLockCode;

    @Expose
    @Column(unique = true, nullable = false)
    protected String email;

    @Expose
    protected String firstName;

    @Expose
    protected String lastName;

    @Expose
    @ManyToOne
    protected Country country;

    @Expose
    @Column
    protected Date dob;

    @Deprecated
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(length = Globals.DB_PIC_MAX_BYTE_SIZE)
    private byte[] userpic;

    @Expose
    @Column
    private String userpicUrl;

    @Column
    private boolean bot;

    //end set by user from mobile

    protected User() {
        enabled = true;
    }

    protected User(long id) {
        this();
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public String toString() {
        return "User{" +
               "email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", dob=" + dob +
               '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public UserBalance getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(UserBalance userBalance) {
        this.userBalance = userBalance;
    }

    public String getUserpicUrl() {
        return userpicUrl;
    }

    public void setUserpicUrl(String userpicUrl) {
        this.userpicUrl = userpicUrl;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Deprecated
    public byte[] getUserpic() {
        return userpic;
    }

    @Deprecated
    public void setUserpic(byte[] userpic) {
        this.userpic = userpic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLostPasswdCode() {
        return lostPasswdCode;
    }

    public void setLostPasswdCode(String lostPasswdCode) {
        this.lostPasswdCode = lostPasswdCode;
    }

    public String getNewMail() {
        return newMail;
    }

    public void setNewMail(String newMail) {
        this.newMail = newMail;
    }

    public String getMd5newMail() {
        return md5newMail;
    }

    public void setMd5newMail(String md5newMail) {
        this.md5newMail = md5newMail;
    }
}
