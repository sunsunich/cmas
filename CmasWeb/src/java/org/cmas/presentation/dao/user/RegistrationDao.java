package org.cmas.presentation.dao.user;

import org.cmas.presentation.entities.user.Registration;
import org.cmas.util.dao.HibernateDao;

import java.util.List;


public interface RegistrationDao extends HibernateDao<Registration> {

    boolean isEmailUnique(String email, Long id);    
    boolean isEmailUnique(String email);

    /**
     * Ищем регистрацию клиента
     * @param id - идентификатор клиента
     * @param sec - md5 - код регистрации
     * @return данные клиента
     */
    Registration getByIdAndSec(Long id, String sec);

    /**
     * Возвращает список клиентов, готовых к прохождению регистрации
     * @return
     */
    List<Registration> getReadyToRegister();

}
