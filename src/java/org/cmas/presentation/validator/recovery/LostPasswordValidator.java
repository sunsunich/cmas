package org.cmas.presentation.validator.recovery;

import org.cmas.presentation.entities.user.UserClient;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.Errors;
import org.cmas.presentation.model.recovery.LostPasswordFormObject;
import org.cmas.util.text.StringUtil;


public class LostPasswordValidator extends RecoveryValidator{

    @Override
    public boolean supports(Class clazz) {
        return LostPasswordFormObject.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors result) {
        super.validate(target,result);
        LostPasswordFormObject formObject = (LostPasswordFormObject)target;
        String email = StringUtil.trim(formObject.getEmail());
        if (email != null) {
			@Nullable
            UserClient user = userDao.getByEmail(email);
			if (user == null) {
				result.rejectValue("email", "validation.cantFindEmail");
			}
		}
    }
}
