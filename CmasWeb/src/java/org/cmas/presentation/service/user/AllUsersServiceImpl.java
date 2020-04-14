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
import org.cmas.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

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
        String lowerCaseEmail = StringUtil.lowerCaseEmail(email);
        boolean isEmailUnique = true;
        switch (role) {
            case ROLE_FEDERATION_ADMIN:
                //fall through
            case ROLE_ADMIN:
                //fall through
            case ROLE_DIVER:
                isEmailUnique = diverDao.isEmailUnique(lowerCaseEmail, userId)
                                || diverDao.isEmailUnique(lowerCaseEmail)
                ;
                break;
            case ROLE_AMATEUR:
                isEmailUnique = amateurDao.isEmailUnique(lowerCaseEmail, userId)
                                || athleteDao.isEmailUnique(lowerCaseEmail)
                ;
                break;
            case ROLE_ATHLETE:
                isEmailUnique = athleteDao.isEmailUnique(lowerCaseEmail, userId)
                                || amateurDao.isEmailUnique(lowerCaseEmail)
                ;
                break;
        }
        return isEmailUnique && registrationDao.isEmailUnique(lowerCaseEmail);
    }

    @Nullable
    @Override
    public User getByEmail(String email) {
        String lowerCaseEmail = StringUtil.lowerCaseEmail(email);
        Athlete athlete = athleteDao.getByEmail(lowerCaseEmail);
        if (athlete != null) {
            return athlete;
        }
        Amateur amateur = amateurDao.getByEmail(lowerCaseEmail);
        if (amateur != null) {
            return amateur;
        }
        Diver diver = diverDao.getByEmail(lowerCaseEmail);
        if (diver != null) {
            return diver;
        }
        return null;
    }
}
