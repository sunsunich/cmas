package org.cmas.presentation.service;

import org.cmas.presentation.entities.user.UserClient;
import org.cmas.util.presentation.CommonAuthentificationServiceImpl;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.util.presentation.Role;


public class AuthenticationServiceImpl extends CommonAuthentificationServiceImpl<UserClient>
        implements AuthenticationService {

    @Autowired
    private UserDao userDao;


    @Override
    @Nullable
    public UserClient getCurrentUser() {
        UserDetails details = getUserDetails();
        if (details == null) {
            return null;
        } else {
            return userDao.getByEmail(details.getUsername());
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
		UserClient user = userDao.getByEmail(userName);
		if (user == null) {
			throw new UsernameNotFoundException("No user with name: " + userName);
		}
//		GrantedAuthority[] roles = Role.getAuthorities(new Role[]{user.getRole()});
//            return new org.springframework.security.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
//                    true, true, true, roles);
		return user;
    }

    @Override
    public boolean isAdmin() {
        Authentication currentAuthentication = getCurrentAuthentication();
        return currentAuthentication != null && isGranted(currentAuthentication, Role.ROLE_ADMIN);
    }

}
