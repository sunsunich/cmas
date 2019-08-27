package org.cmas.presentation.service.user;

import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Mar 21, 2016
 *
 * @author Alexander Petukhov
 */
public interface PersonalCardService {

    void setupDisplayCardsForDivers(List<Diver> divers);

    List<PersonalCard> getCardsToShow(Diver diver);

    void generateNonPrimaryCardsImages(CardUser cardUser);

    <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao);

    PersonalCard generateAndSaveCardImage(long personalCardId);
}
