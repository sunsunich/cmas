package org.cmas.presentation.dao.cards;

import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.dao.HibernateDao;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PersonalCardDao extends HibernateDao<PersonalCard>{

    @Nullable
    PersonalCard getByNumber(@Nullable NationalFederation federation, String cardNumber);

    void deleteDiverCards(Diver diver);

    List<PersonalCard> getNationalCardsOrdered(Diver diver);
}
