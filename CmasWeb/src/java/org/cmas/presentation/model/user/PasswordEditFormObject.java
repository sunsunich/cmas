package org.cmas.presentation.model.user;

import org.cmas.presentation.model.PasswordFormObject;
import org.hibernate.validator.NotEmpty;

public class PasswordEditFormObject extends PasswordFormObject {

    @NotEmpty(message = "validation.passwordEmpty")
    private String oldPassword;	

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}