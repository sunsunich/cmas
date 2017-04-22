package org.cmas.presentation.dao.user;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.util.dao.HibernateDao;

public interface PersonalCardDao extends HibernateDao<PersonalCard>{

    PersonalCard getByNumber(String cardNumber);

    void deleteDiverCards(Diver diver);
}
