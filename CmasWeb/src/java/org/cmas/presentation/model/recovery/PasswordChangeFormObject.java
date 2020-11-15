package org.cmas.presentation.model.recovery;

import org.cmas.presentation.model.PasswordFormObject;
import org.hibernate.validator.NotEmpty;

import javax.annotation.Nullable;


public class PasswordChangeFormObject extends PasswordFormObject {

    @Nullable
    @NotEmpty
    private String code;

    @Nullable
    public String getCode() {
        return code;
    }

    public void setCode(@Nullable String code) {
        this.code = code;
    }

}
