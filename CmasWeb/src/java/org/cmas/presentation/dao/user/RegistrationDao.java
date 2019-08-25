package org.cmas.presentation.dao.user;

import org.cmas.presentation.entities.user.Registration;
import org.cmas.util.dao.HibernateDao;

import java.util.List;


public interface RegistrationDao extends HibernateDao<Registration> {

    boolean isEmailUnique(String email, Long id);    
    boolean isEmailUnique(String email);

    Registration getBySec(String sec);

    List<Registration> getReadyToRegister();
}
