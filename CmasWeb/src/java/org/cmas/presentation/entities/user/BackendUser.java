package org.cmas.presentation.entities.user;

import org.cmas.Globals;
import org.cmas.entities.User;
import org.cmas.presentation.entities.InternetAddressOwner;
import org.cmas.util.presentation.SpringRole;
import org.cmas.util.presentation.UserAuthority;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Objects;


public class BackendUser<T extends User> implements UserDetails, InternetAddressOwner {

    private T user;

    public BackendUser(T user) {
        this.user = user;
    }

    @Override
    public Long getNullableId() {
        return user.getId();
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        @SuppressWarnings({"CallToSimpleGetterFromWithinClass"})
        String personal = getUsername();
        return new InternetAddress(user.getEmail(), personal, Globals.UTF_8_ENC);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return new GrantedAuthority[]{new UserAuthority(SpringRole.fromRole(user.getRole()))};
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + getNullableId() +
                ", email='" + user.getEmail() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackendUser)) return false;
        BackendUser that = (BackendUser) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }


    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
