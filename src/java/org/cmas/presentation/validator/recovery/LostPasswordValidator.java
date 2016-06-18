package org.cmas.presentation.validator.recovery;

import org.cmas.entities.User;
import org.cmas.presentation.model.recovery.LostPasswordFormObject;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.Errors;


public class LostPasswordValidator extends RecoveryValidator{

    @Override
    public boolean supports(Class clazz) {
        return LostPasswordFormObject.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors result) {
        super.validate(target,result);
        LostPasswordFormObject formObject = (LostPasswordFormObject)target;
        String email = StringUtil.correctSpaceCharAndTrim(formObject.getEmail());
        if (email != null) {
			@Nullable
            User user = allUsersService.getByEmail(email);
			if (user == null) {
				result.rejectValue("email", "validation.cantFindEmail");
			}
		}
    }
}
