package org.cmas.presentation.service.cards;

import org.cmas.entities.cards.CardUser;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.dao.HibernateDao;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created on Mar 21, 2016
 *
 * @author Alexander Petukhov
 */
public interface PersonalCardService {

    void setupDisplayCardsForDivers(List<Diver> divers);

    List<PersonalCard> getCardsToShow(Diver diver);

    @Nullable
    PersonalCard getMaxNationalCard(Diver diver);

    @Nullable
    PersonalCard getMaxCard(Diver diver);

    void generateNonPrimaryCardsImages(CardUser cardUser);

    <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao, boolean generateImage);

    PersonalCard generateAndSaveCardImage(long personalCardId);

    boolean canFederationEditCard(NationalFederation nationalFederation, PersonalCardType cardType);
}
