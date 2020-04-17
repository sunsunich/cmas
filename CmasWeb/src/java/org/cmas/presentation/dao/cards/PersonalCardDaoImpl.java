package org.cmas.presentation.dao.cards;

import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class PersonalCardDaoImpl extends HibernateDaoImpl<PersonalCard> implements PersonalCardDao {

    @Nullable
    @Override
    public PersonalCard getByNumber(@Nullable NationalFederation federation, String cardNumber) {
        Criteria criteria;
        if (federation == null) {
            criteria = createCriteria()
                    .add(Restrictions.eq("number", cardNumber));
        } else {
            criteria = createCriteria()
                    .createAlias("diver", "d")
                    .add(Restrictions.eq("number", cardNumber))
                    .add(Restrictions.eq("d.federation", federation));
        }
        @SuppressWarnings("unchecked")
        List<PersonalCard> cards = criteria.list();
        return cards.isEmpty() ? null : cards.get(0);
    }

    @Override
    public void deleteDiverCards(Diver diver) {
        String hql = "delete from org.cmas.entities.cards.PersonalCard c"
                     + " where c.diver = :diver and c.cardType != :cardType";
        createQuery(hql).setEntity("diver", diver).setParameter("cardType", PersonalCardType.PRIMARY)
                        .executeUpdate();
    }

    @Override
    public List<PersonalCard> getNationalCardsOrdered(Diver diver) {
        //noinspection unchecked
        return createCriteria()
                .add(Restrictions.eq("diver", diver))
                .add(Restrictions.eq("cardType", PersonalCardType.NATIONAL))
                .addOrder(Order.desc("id"))
                .list();
    }
}
