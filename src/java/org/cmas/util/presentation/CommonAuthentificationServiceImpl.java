package org.cmas.util.presentation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;

import java.util.Arrays;

public abstract class CommonAuthentificationServiceImpl<T extends UserDetails> implements CommonAuthentificationService<T>{

    @Override
    public void loginAs(T user, SpringRole[] roles) {
        GrantedAuthority[] gas = SpringRole.getAuthorities(roles);
        SecurityContext ctx = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "", gas);
        ctx.setAuthentication(token);
    }

    @Nullable
    protected UserDetails getUserDetails() {
        Authentication auth = getCurrentAuthentication();
        if (auth == null) {
            return null;
        }
        Object obj = auth.getPrincipal();
        if (obj instanceof UserDetails) {
            return (UserDetails) obj;
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Authentication getCurrentAuthentication() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication();
    }


    @Override
    public boolean isGranted(@NotNull Authentication auth, SpringRole role) {
        return Arrays.asList(auth.getAuthorities()).contains(role.getAuthority());

    }
}
