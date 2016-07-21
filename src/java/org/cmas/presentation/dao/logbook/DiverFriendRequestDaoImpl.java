package org.cmas.presentation.dao.logbook;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("unchecked")
public class DiverFriendRequestDaoImpl extends HibernateDaoImpl<DiverFriendRequest> implements DiverFriendRequestDao {

    @Override
    public boolean hasDiverFriendRequest(Diver from, Diver to) {
        Object result = createSearchCriteria(from, to)
                .setProjection(Projections.rowCount())
                .uniqueResult();
        return result != null && ((Number) result).intValue() > 0;
    }

    private Criteria createSearchCriteria(Diver from, Diver to) {
        return createCriteria()
                .add(Restrictions.eq("from", from))
                .add(Restrictions.eq("to", to));
    }

    @Override
    public DiverFriendRequest getDiverFriendRequest(Diver from, Diver to) {
        return (DiverFriendRequest) createSearchCriteria(from, to)
                .uniqueResult();
    }

    @Override
    public List<DiverFriendRequest> getRequestsFromDiver(Diver diver) {
        return createCriteria()
                .add(Restrictions.eq("from", diver))
                .list();
    }

    @Override
    public List<DiverFriendRequest> getRequestsToDiver(Diver diver) {
        return createCriteria()
                .add(Restrictions.eq("to", diver))
                .list();
    }
}
