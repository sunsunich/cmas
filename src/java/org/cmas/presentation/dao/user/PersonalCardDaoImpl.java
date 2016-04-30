package org.cmas.presentation.dao.user;

import org.cmas.entities.PersonalCard;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class PersonalCardDaoImpl extends HibernateDaoImpl<PersonalCard> implements PersonalCardDao {


    @Override
    public PersonalCard getByNumber(String cardNumber) {
        return (PersonalCard) createCriteria().add(Restrictions.eq("number", cardNumber)).uniqueResult();
    }
}
