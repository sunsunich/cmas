package org.cmas.presentation.model;

import org.cmas.presentation.validator.Validatable;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;


public class PasswordFormObject implements Validatable{

	@NotEmpty(message = "validation.passwordEmpty")
    protected String password;

    @NotEmpty(message = "validation.checkPasswordEmpty")
    protected String checkPassword;

	@Override
    public void validate(Errors errors) {
        if(!password.equals(checkPassword)){
            errors.rejectValue("checkPassword", "validation.passwordMismatch");
        }
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckPassword() {
		return checkPassword;
	}

	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}
}
