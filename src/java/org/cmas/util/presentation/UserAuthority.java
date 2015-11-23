package org.cmas.util.presentation;

import org.springframework.security.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {

    SpringRole role;
    private static final long serialVersionUID = -4086288513335428033L;

    public UserAuthority(SpringRole role) {
        this.role = role;
    }

    UserAuthority() {
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    @Override
    public int compareTo(Object o) {
        return role.compareTo((SpringRole) o);
    }
}
