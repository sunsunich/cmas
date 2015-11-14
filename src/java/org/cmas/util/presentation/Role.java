package org.cmas.util.presentation;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public enum Role {
    ROLE_USER("ROLE_USER", "пользователь"), ROLE_VIP_USER("ROLE_VIP_USER", "VIP-пользователь"),
    ROLE_ADMIN("ROLE_ADMIN", "администратор");


    private final String name;
    private final String label;

    private final GrantedAuthority authority;

    Role(String name, String label) {
        this.name = name;
        this.label = label;
        authority = new GrantedAuthorityImpl(name());
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }

    public static GrantedAuthority[] getAuthorities(Role[] roles) {
        GrantedAuthority[] result = new GrantedAuthority[roles.length];
        int i = 0;
        for (Role role : roles) {
            result[i] = role.authority;
            i++;
        }
        return result;
    }
}