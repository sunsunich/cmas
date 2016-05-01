package org.cmas.presentation.service.user;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class AllUsersServiceImpl implements AllUsersService {

    @Autowired
    private AthleteDao athleteDao;

    @Autowired
    private AmateurDao amateurDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private RegistrationDao registrationDao;

    @Override
    public boolean isEmailUnique(Role role, @Nullable Long userId, String email) {
        boolean isEmailUnique = true;
        switch (role) {
            case ROLE_ADMIN:
                //fall through
            case ROLE_DIVER:
                isEmailUnique = diverDao.isEmailUnique(email, userId)
                                || diverDao.isEmailUnique(email)
                ;
                break;
            case ROLE_AMATEUR:
                isEmailUnique = amateurDao.isEmailUnique(email, userId)
                                || athleteDao.isEmailUnique(email)
                ;
                break;
            case ROLE_ATHLETE:
                isEmailUnique = athleteDao.isEmailUnique(email, userId)
                                || amateurDao.isEmailUnique(email)
                ;
                break;
        }
        return isEmailUnique && registrationDao.isEmailUnique(email);
    }

    @Override
    public User getByEmail(String email) {
        Athlete athlete = athleteDao.getByEmail(email);
        if (athlete != null) {
            return athlete;
        }
        Amateur amateur = amateurDao.getByEmail(email);
        if (amateur != null) {
            return amateur;
        }
        Diver diver = diverDao.getByEmail(email);
        if (diver != null) {
            return diver;
        }
        return null;
    }
}
