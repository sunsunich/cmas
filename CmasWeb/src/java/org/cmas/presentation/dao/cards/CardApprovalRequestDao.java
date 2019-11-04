package org.cmas.presentation.dao.cards;

import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.diver.Diver;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public interface CardApprovalRequestDao extends HibernateDao<CardApprovalRequest> {

    List<CardApprovalRequest> getPendingByDiver(Diver diver);
}
