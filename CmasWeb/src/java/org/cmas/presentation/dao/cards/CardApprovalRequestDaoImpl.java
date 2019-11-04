package org.cmas.presentation.dao.cards;

import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.entities.diver.Diver;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestDaoImpl extends HibernateDaoImpl<CardApprovalRequest> implements CardApprovalRequestDao {

    @Override
    public List<CardApprovalRequest> getPendingByDiver(Diver diver) {
        //noinspection unchecked
        return createCriteria().add(Restrictions.eq("diver", diver))
                               .add(Restrictions.eq("status", CardApprovalRequestStatus.NEW))
                               .add(Restrictions.eq("deleted", false))
                               .list();
    }
}
