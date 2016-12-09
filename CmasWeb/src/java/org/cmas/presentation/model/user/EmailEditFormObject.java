package org.cmas.presentation.model.user;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;

public class EmailEditFormObject {

	@NotEmpty(message = "validation.passwordEmpty")
	private String password;

    @Email
    @NotEmpty(message = "validation.emailEmpty")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}