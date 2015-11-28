package org.cmas.presentation.service.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.sport.Sportsman;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.SportsmanDao;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class AllUsersServiceImpl implements AllUsersService {

    @Autowired
    private SportsmanDao sportsmanDao;

    @Autowired
    private AmateurDao amateurDao;

    @Autowired
    private RegistrationDao registrationDao;

    @Override
    public boolean isEmailUnique(Role role, @Nullable Long userId, String email) {
        boolean isEmailUnique = true;
        switch (role) {
            case ROLE_ADMIN:
                //fall through
            case ROLE_AMATEUR:
                isEmailUnique = amateurDao.isEmailUnique(email, userId)
                        || sportsmanDao.isEmailUnique(email)
                ;
                break;
            case ROLE_SPORTSMAN:
                isEmailUnique = sportsmanDao.isEmailUnique(email, userId)
                        || amateurDao.isEmailUnique(email)
                ;
                break;
        }
        return isEmailUnique && registrationDao.isEmailUnique(email);
    }

    @Override
    public User getByEmail(String email) {
        Sportsman sportsman = sportsmanDao.getByEmail(email);
        if(sportsman == null){
            return amateurDao.getByEmail(email);
        }
        return sportsman;
    }
}
