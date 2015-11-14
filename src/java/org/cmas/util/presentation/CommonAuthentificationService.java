package org.cmas.util.presentation;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

public interface CommonAuthentificationService<T extends UserDetails> extends UserDetailsService{

    void loginAs(T user, Role[] roles);

    @Nullable
    T getCurrentUser();

    Authentication getCurrentAuthentication();

    boolean isGranted(Authentication auth, Role role);
}
