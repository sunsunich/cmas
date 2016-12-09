package org.cmas.presentation.service.user;

public interface PasswordService {

    PasswordStrength measurePasswordStrength(String password);

    String generatePassword();
}
