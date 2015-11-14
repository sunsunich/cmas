package org.cmas.presentation.model.recovery;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Email;


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
