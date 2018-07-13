package org.cmas.presentation.dao.user;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.diver.Diver;
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

    @Override
    public void deleteDiverCards(Diver diver) {
        String hql = "delete from org.cmas.entities.PersonalCard c where c.diver = :diver and c.cardType != :cardType";
        createQuery(hql).setEntity("diver", diver).setParameter("cardType", PersonalCardType.PRIMARY)
                        .executeUpdate();
    }
}
