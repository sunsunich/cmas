package org.cmas.presentation.model.admin;

import org.hibernate.validator.NotEmpty;

/**
 * Created on Apr 06, 2020
 *
 * @author Alexander Petukhov
 */
public class FederationFormObject {

    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
