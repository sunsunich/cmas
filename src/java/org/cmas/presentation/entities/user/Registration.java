package org.cmas.presentation.entities.user;

import org.cmas.Globals;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Locale;


@Entity
@Table(name = "registration")
public class Registration implements InternetAddressOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Дата регистрации
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private Date dateReg;

    // E-mail пользователя
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String email;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String country;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String role;

    // Пароль
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String password;
    // Для подтверждения регистрации
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String md5;

    //локаль с которрой регился
    @Column(nullable = false)
    private Locale locale;

    public Registration() {
    }

    public Registration(Date dateReg) {
        this.dateReg = dateReg;
    }

    @Override
    public Long getNullableId() {
        return id;
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        String personal = email;
        return new InternetAddress(email, personal, "UTF-8");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String city) {
        this.country = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
