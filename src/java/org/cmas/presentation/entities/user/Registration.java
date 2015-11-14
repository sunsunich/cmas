package org.cmas.presentation.entities.user;

import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.Globals;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private String phone;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String shopName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String city;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private String webAddress;

    // Пароль
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String password;
    // Для подтверждения регистрации
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String md5;
    // Регистрация подтверждена?
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    private boolean confirm;
    // Дата подтверждения регистрации
    private Date dateConfirm;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDateReg() {
        return dateReg;
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
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

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
