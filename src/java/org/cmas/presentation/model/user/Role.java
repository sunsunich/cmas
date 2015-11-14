package org.cmas.presentation.model.user;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентский роли в системе.
 *
 * енум хранится в базе в качестве битмаски, поэтому изменять уже существующие элементы можно только
 * при апдейте соответствующиих битов в table.
 *
 */
public enum Role {

    ROLE_DEFAULT("Доступ к базовым функциям"),

    ROLE_PROFILE("Управление личными данными"),
    ROLE_ADMIN("Администратор системы");

    private final GrantedAuthority authority;
    private final long bitMask;
    private final String humanName;

    Role(String humanName) {
        authority = new GrantedAuthorityImpl(name());
        bitMask = 1L << ordinal();
        this.humanName = humanName;
    }

    public static Role[] getRoles(long mask) {
        List<Role> result = new ArrayList<Role>();
        Role[] roles = values();
        for (Role role : roles) {
            if ((role.bitMask & mask) != 0) {
                result.add(role);
            }
        }
        return result.toArray(new Role[result.size()]);
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

    public static long getMask(Role[] roles) {
        long result = 0;
        for (Role role : roles) {
            result |= role.bitMask;
        }
        return result;
    }    

    public GrantedAuthority getAuthority() {
        return authority;
    }

    public String getHumanName() {
        return humanName;
    }

    public String getAcegiRoleName() {
        return name();
    }

    public String getJsRoleName() {
        String name = name().toLowerCase();
        //if (name.startsWith("role_")) {
        //    name = name.substring(5);
        //}
        String[] parts = StringUtils.delimitedListToStringArray(name, "_");
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String s = StringUtils.capitalize(parts[i]);
            result.append(s);
        }
        return result.toString();
    }

}
