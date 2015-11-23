package org.cmas.util.presentation;

import org.cmas.entities.Role;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public enum SpringRole {
    ROLE_AMATEUR(Role.ROLE_AMATEUR), ROLE_SPORTSMAN(Role.ROLE_SPORTSMAN),
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
            case ROLE_SPORTSMAN:
                return ROLE_SPORTSMAN;
            case ROLE_ADMIN:
                return ROLE_ADMIN;
        }
        throw new IllegalStateException();
    }
}