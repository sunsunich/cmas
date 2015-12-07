package org.cmas.util.presentation;

import org.cmas.entities.Role;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public enum SpringRole {
    ROLE_AMATEUR(Role.ROLE_AMATEUR),
    ROLE_ATHLETE(Role.ROLE_ATHLETE),
    ROLE_DIVER(Role.ROLE_DIVER),
    ROLE_DIVER_INSTRUCTOR(Role.ROLE_DIVER_INSTRUCTOR),
    ROLE_ADMIN(Role.ROLE_ADMIN);

    private final GrantedAuthority authority;

    SpringRole(Role role) {
        authority = new GrantedAuthorityImpl(role.name());
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }

    public static GrantedAuthority[] getAuthorities(SpringRole[] roles) {
        GrantedAuthority[] result = new GrantedAuthority[roles.length];
        int i = 0;
        for (SpringRole role : roles) {
            result[i] = role.authority;
            i++;
        }
        return result;
    }

    public static SpringRole fromRole(Role role) {
        switch (role) {
            case ROLE_AMATEUR:
                return ROLE_AMATEUR;
            case ROLE_ATHLETE:
                return ROLE_ATHLETE;
            case ROLE_DIVER:
                return ROLE_DIVER;
            case ROLE_DIVER_INSTRUCTOR:
                return ROLE_DIVER_INSTRUCTOR;
            case ROLE_ADMIN:
                return ROLE_ADMIN;
            default:
                throw new IllegalStateException();
        }

    }
}