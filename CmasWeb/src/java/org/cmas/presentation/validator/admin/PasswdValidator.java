package org.cmas.presentation.validator.admin;

import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.util.text.StringUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class PasswdValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return PasswordChangeFormObject.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors result) {
        PasswordChangeFormObject model = (PasswordChangeFormObject) target;
        if (StringUtil.isEmpty(model.getPasswd())) {
            result.rejectValue("passwd", "validation.emptyField");
        }
        if (StringUtil.isEmpty(model.getPasswdRe())) {
            result.rejectValue("passwdRe", "validation.emptyField");
        } else {
            if (!model.getPasswd().equals(model.getPasswdRe())) {
                result.rejectValue("passwd", "validation.passwordMismatch");
            } else {
            }
        }
    }
}
