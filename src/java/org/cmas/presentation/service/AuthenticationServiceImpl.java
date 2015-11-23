package org.cmas.presentation.service;

import org.cmas.entities.sport.Sportsman;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.sport.SportsmanDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.util.presentation.CommonAuthentificationServiceImpl;
import org.cmas.util.presentation.SpringRole;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;


public class AuthenticationServiceImpl extends CommonAuthentificationServiceImpl<BackendUser>
        implements AuthenticationService {

    @Autowired
    private SportsmanDao sportsmanDao;

    @Autowired
    private AmateurDao amateurDao;


    @Override
    @Nullable
    public BackendUser<Sportsman> getCurrentSportsman() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            return new BackendUser<Sportsman>(sportsmanDao.getByEmail(details.getUsername()));
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
		BackendUser user = new BackendUser(sportsmanDao.getByEmail(userName));
		if (user == null) {
			throw new UsernameNotFoundException("No user with name: " + userName);
		}
//		GrantedAuthority[] roles = SpringRole.getAuthorities(new SpringRole[]{user.getRole()});
//            return new org.springframework.security.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
//                    true, true, true, roles);
		return user;
    }

    @Override
    public boolean isAdmin() {
        Authentication currentAuthentication = getCurrentAuthentication();
        return currentAuthentication != null && isGranted(currentAuthentication, SpringRole.ROLE_ADMIN);
    }

}
