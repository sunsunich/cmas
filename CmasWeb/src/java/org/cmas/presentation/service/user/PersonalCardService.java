package org.cmas.presentation.service.user;

import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;
import org.cmas.util.dao.HibernateDao;

/**
 * Created on Mar 21, 2016
 *
 * @author Alexander Petukhov
 */
public interface PersonalCardService {

    <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao);

    PersonalCard generateAndSaveCardImage(long personalCardId);
}
