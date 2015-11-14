package org.cmas.presentation.entities.user;

import org.cmas.entities.User;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.util.presentation.Role;
import org.cmas.util.presentation.UserAuthority;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 1.ФИО
 * 2.дата рождения
 * 3.страна
 * 4.город
 * 5.контактные данные (skype,  ICQ,  mobile)
 */
@Entity
public class UserClient extends User implements UserDetails, InternetAddressOwner {

//служебные данные

    // Дата регистрации
    @org.hibernate.validator.NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private Date dateReg;

    @Column
    private boolean enabled;

    @Column(name = "last_action")
    private Date lastAction;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


    //служебные и обязательные данные
    @Column
    private String lostPasswdCode;

    // Новая почта пользователя
    private String newMail;

    // Код для смены почтового адреса
    @Column(length = 32)
    private String md5newMail;

    //конец всем данным

    private static final long serialVersionUID = -838023365477426695L;


    public UserClient(long id) {
        super();
        this.id = id;
    }

    @Override
    public Long getNullableId() {
        return id;
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        @SuppressWarnings({"CallToSimpleGetterFromWithinClass"})
        String personal = getUsername();
        return new InternetAddress(getEmail(), personal, "UTF-8");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[]{new UserAuthority(role)};
    }

    public String getLostPasswdCode() {
        return lostPasswdCode;
    }

    public void setLostPasswdCode(String lostPasswdCode) {
        this.lostPasswdCode = lostPasswdCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + getNullableId() +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserClient user = (UserClient) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public String getRoleName() {
        return role.toString();
    }

    public Date getLastAction() {
        return lastAction;
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

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }
}
