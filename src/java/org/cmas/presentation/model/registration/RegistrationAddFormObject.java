package org.cmas.presentation.model.registration;

import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.PasswordFormObject;
import org.cmas.presentation.model.Transferable;
import org.cmas.Globals;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

import javax.persistence.Column;
import java.util.Locale;


public class RegistrationAddFormObject extends PasswordFormObject
        implements Transferable<Registration> {

    @NotEmpty(message = "validation.emptyField")
    private String shopName;

    @NotEmpty(message = "validation.emptyField")
    private String city;

    @NotEmpty(message = "validation.emptyField")
    private String webAddress;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotNull(message = "validation.emptyField")
    @Column(nullable = false)
    @Pattern( regex = Globals.PHONE_REGEXP
            , message = "validation.phoneValid"
    )
    private String phone;

    @NotEmpty(message = "validation.emailEmpty")
    @Pattern( regex = Globals.EMAIL_REGEXP
            , message = "validation.emailValid"
            , flags = java.util.regex.Pattern.CASE_INSENSITIVE
    )
    private String email;

    private Locale locale;

    @Override
    public void transferToEntity(Registration entity) {
        entity.setPassword(password);
        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setShopName(shopName);
        entity.setCity(city);
        entity.setWebAddress(webAddress);
        entity.setLocale(locale);
    }

    @Override
    public void transferFromEntity(Registration entity) {
        password = entity.getPassword();
        email = entity.getEmail();
        phone = entity.getPhone();
        shopName = entity.getShopName();
        city = entity.getCity();
        webAddress = entity.getWebAddress();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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
}
