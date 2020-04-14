package org.cmas.presentation.validator.recovery;

import org.cmas.entities.User;
import org.cmas.presentation.model.recovery.LostPasswordFormObject;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.Errors;


public class LostPasswordValidator extends RecoveryValidator {

    @Override
    public boolean supports(Class clazz) {
        return LostPasswordFormObject.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors result) {
        super.validate(target, result);
        LostPasswordFormObject formObject = (LostPasswordFormObject) target;
        String emailStr = formObject.getEmail();
        if (StringUtil.isTrimmedEmpty(emailStr)) {
            result.rejectValue("email", "validation.emptyField");
        } else {
            String email = StringUtil.lowerCaseEmail(formObject.getEmail());
            @Nullable
            User user = allUsersService.getByEmail(email);
            if (user == null) {
                result.rejectValue("email", "validation.cantFindEmail");
            }
        }
    }
}
