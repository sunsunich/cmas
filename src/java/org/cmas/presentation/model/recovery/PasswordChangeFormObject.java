package org.cmas.presentation.model.recovery;

import org.hibernate.validator.NotEmpty;
import org.cmas.presentation.model.PasswordFormObject;


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
