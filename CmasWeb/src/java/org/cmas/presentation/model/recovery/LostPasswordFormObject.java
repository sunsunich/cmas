package org.cmas.presentation.model.recovery;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;


public class LostPasswordFormObject {

	@Email
	@NotEmpty(message = "validation.emailEmpty")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
