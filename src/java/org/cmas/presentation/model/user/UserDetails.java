package org.cmas.presentation.model.user;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.User;


public class UserDetails extends User {
    public static final String SALT = "f5r$m#%G0";

    // User id
    private Long id;

    public UserDetails(String username, String password, boolean enabled,
                         boolean accountNotExpired, boolean credentialsNotExpired,
                         boolean accountNotLocked, GrantedAuthority[] authorities, Long id)
            throws IllegalArgumentException {
        super(username, password, enabled, accountNotExpired, credentialsNotExpired, accountNotLocked, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                '}';
    }

}
