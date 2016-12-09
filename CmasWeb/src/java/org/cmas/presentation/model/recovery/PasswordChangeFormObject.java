package org.cmas.presentation.model.recovery;

import org.cmas.presentation.model.PasswordFormObject;
import org.hibernate.validator.NotEmpty;


public class PasswordChangeFormObject extends PasswordFormObject {

    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
