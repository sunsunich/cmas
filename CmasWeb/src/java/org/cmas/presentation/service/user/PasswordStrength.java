package org.cmas.presentation.service.user;

public enum PasswordStrength {

    NONE, VERY_WEAK, WEAK, AVERAGE, STRONG, VERY_STRONG, SECURE, VERY_SECURE;

    public String getName(){
        return name();
    }
}
