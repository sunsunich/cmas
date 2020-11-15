package org.cmas.presentation.model;

import org.cmas.presentation.validator.Validatable;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

import javax.annotation.Nullable;


public class PasswordFormObject implements Validatable{

	@Nullable
	@NotEmpty(message = "validation.passwordEmpty")
    protected String password;

	@Nullable
    @NotEmpty(message = "validation.checkPasswordEmpty")
    protected String checkPassword;

	@Override
	public void validate(Errors errors) {
		if (password != null && !password.equals(checkPassword)) {
			errors.rejectValue("checkPassword", "validation.passwordMismatch");
		}
	}

	@Nullable
	public String getPassword() {
		return password;
	}

	public void setPassword(@Nullable String password) {
		this.password = password;
	}

	@Nullable
	public String getCheckPassword() {
		return checkPassword;
	}

	public void setCheckPassword(@Nullable String checkPassword) {
		this.checkPassword = checkPassword;
	}
}
