package org.cmas.presentation.service;

import org.cmas.entities.User;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.user.AllUsersService;
import org.cmas.util.presentation.CommonAuthentificationServiceImpl;
import org.cmas.util.presentation.SpringRole;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


public class AuthenticationServiceImpl extends CommonAuthentificationServiceImpl<BackendUser>
        implements AuthenticationService {

    @Autowired
    private AthleteDao athleteDao;

    @Autowired
    private AmateurDao amateurDao;

    @Autowired
    private AllUsersService allUsersService;

    @Override
    @Nullable
    public BackendUser<Athlete> getCurrentAthlete() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            Athlete athlete = athleteDao.getByEmail(details.getUsername());
            if (athlete != null) {
                return new BackendUser<Athlete>(athlete);
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
            if (Arrays.asList(details.getAuthorities()).contains(SpringRole.ROLE_ATHLETE.getAuthority())) {
                Athlete athlete = athleteDao.getByEmail(details.getUsername());
                if (athlete != null) {
                    return new BackendUser<Athlete>(athlete);
                } else {
                    return null;
                }
            } else {
                Amateur amateur = amateurDao.getByEmail(details.getUsername());
                if (amateur != null) {
                    return new BackendUser<Amateur>(amateur);
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        User user = allUsersService.getByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("No user with name: " + userName);
        }
        BackendUser backendUser = new BackendUser(user);

//		GrantedAuthority[] roles = SpringRole.getAuthorities(new SpringRole[]{user.getRole()});
//            return new org.springframework.security.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
//                    true, true, true, roles);
        return backendUser;
    }

    @Override
    public boolean isAdmin() {
        Authentication currentAuthentication = getCurrentAuthentication();
        return currentAuthentication != null && isGranted(currentAuthentication, SpringRole.ROLE_ADMIN);
    }

}
