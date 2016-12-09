package org.cmas.util.presentation;

import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.entities.user.BackendUser;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

public interface CommonAuthenticationService<T extends UserDetails> extends UserDetailsService{

    void loginAs(T user, SpringRole[] roles);

    @Nullable
    BackendUser<Athlete> getCurrentAthlete();

    @Nullable
    BackendUser<Diver> getCurrentDiver();

    @Nullable
    BackendUser<? extends User> getCurrentUser();

    Authentication getCurrentAuthentication();

    boolean isGranted(Authentication auth, SpringRole role);
}
