package org.cmas.util.presentation;

import org.cmas.entities.Role;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public enum SpringRole {
    ROLE_AMATEUR(Role.ROLE_AMATEUR, "/secure/index.html"),
    ROLE_ATHLETE(Role.ROLE_ATHLETE, "/secure/index.html"),
    ROLE_DIVER(Role.ROLE_DIVER, "/secure/index.html"),
    ROLE_FEDERATION_ADMIN(Role.ROLE_FEDERATION_ADMIN, "/fed/index.html"),
    ROLE_ADMIN(Role.ROLE_ADMIN, "/admin/index.html");

    private final GrantedAuthority authority;

    private final String indexUrl;

    SpringRole(Role role, String indexUrl) {
        authority = new GrantedAuthorityImpl(role.name());
        this.indexUrl = indexUrl;
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }

    public String getIndexUrl() {
        return indexUrl;
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
            case ROLE_FEDERATION_ADMIN:
                return ROLE_FEDERATION_ADMIN;
            case ROLE_ADMIN:
                return ROLE_ADMIN;
            default:
                throw new IllegalStateException();
        }

    }
}