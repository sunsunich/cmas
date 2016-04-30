package org.cmas.presentation.service;

import org.cmas.entities.User;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.util.presentation.CommonAuthenticationServiceImpl;
import org.cmas.util.presentation.SpringRole;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


public class AuthenticationServiceImpl extends CommonAuthenticationServiceImpl<BackendUser>
        implements AuthenticationService {

    @Autowired
    private AllUsersService allUsersService;

    @Override
    @Nullable
    public BackendUser<Athlete> getCurrentAthlete() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            Athlete athlete = (Athlete) allUsersService.getByEmail(details.getUsername());
            if (athlete != null) {
                return new BackendUser<>(athlete);
            } else {
                return null;
            }
        }
    }

    @Nullable
    @Override
    public BackendUser<Diver> getCurrentDiver() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            Diver diver = (Diver) allUsersService.getByEmail(details.getUsername());
            if (diver != null) {
                return new BackendUser<>(diver);
            } else {
                return null;
            }
        }
    }

    @Override
    @Nullable
    public BackendUser<? extends User> getCurrentUser() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            List<GrantedAuthority> authorities = Arrays.asList(details.getAuthorities());
            if (authorities.contains(SpringRole.ROLE_DIVER.getAuthority())
                || authorities.contains(SpringRole.ROLE_DIVER_INSTRUCTOR.getAuthority())
                    ) {
                Diver diver = (Diver) allUsersService.getByEmail(details.getUsername());
                if (diver != null) {
                    return new BackendUser<>(diver);
                } else {
                    return null;
                }
            } else if (authorities.contains(SpringRole.ROLE_ATHLETE.getAuthority())) {
                Athlete athlete = (Athlete) allUsersService.getByEmail(details.getUsername());
                if (athlete != null) {
                    return new BackendUser<>(athlete);
                } else {
                    return null;
                }
            } else {
                Amateur amateur = (Amateur) allUsersService.getByEmail(details.getUsername());
                if (amateur != null) {
                    return new BackendUser<>(amateur);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    @Nullable
    public String getCurrentUserName() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            return details.getUsername();
        }
    }

    @Transactional
    @Override
    @Nullable
    public UserDetails loadUserByUsername(String userName) throws DataAccessException {
        User user = allUsersService.getByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("No user with name: " + userName);
        }

//		GrantedAuthority[] roles = SpringRole.getAuthorities(new SpringRole[]{user.getRole()});
//            return new org.springframework.security.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
//                    true, true, true, roles);
        return new BackendUser(user);
    }

    @Override
    public boolean isAdmin() {
        Authentication currentAuthentication = getCurrentAuthentication();
        return currentAuthentication != null && isGranted(currentAuthentication, SpringRole.ROLE_ADMIN);
    }

    @Override
    public boolean isDiver() {
        Authentication currentAuthentication = getCurrentAuthentication();
        return currentAuthentication != null &&
               (isGranted(currentAuthentication, SpringRole.ROLE_DIVER) ||
                isGranted(currentAuthentication, SpringRole.ROLE_DIVER_INSTRUCTOR));
    }

}
