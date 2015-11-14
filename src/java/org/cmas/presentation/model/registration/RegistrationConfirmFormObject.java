package org.cmas.presentation.model.registration;

import org.hibernate.validator.NotNull;

public class RegistrationConfirmFormObject {

    @NotNull
    private long regId;

    @NotNull
    private String sec;

    public long getRegId() {
        return regId;
    }

    public void setRegId(long regId) {
        this.regId = regId;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }
}

