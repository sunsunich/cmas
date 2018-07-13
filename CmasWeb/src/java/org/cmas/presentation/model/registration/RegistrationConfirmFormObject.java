package org.cmas.presentation.model.registration;

import org.hibernate.validator.NotNull;

public class RegistrationConfirmFormObject {

    @NotNull
    private String sec;

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }
}

