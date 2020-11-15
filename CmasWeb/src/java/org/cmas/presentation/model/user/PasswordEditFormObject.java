package org.cmas.presentation.model.user;

import org.cmas.presentation.model.PasswordFormObject;
import org.hibernate.validator.NotEmpty;

import javax.annotation.Nullable;

public class PasswordEditFormObject extends PasswordFormObject {

    @Nullable
    @NotEmpty(message = "validation.passwordEmpty")
    private String oldPassword;

    @Nullable
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@Nullable String oldPassword) {
        this.oldPassword = oldPassword;
    }
}