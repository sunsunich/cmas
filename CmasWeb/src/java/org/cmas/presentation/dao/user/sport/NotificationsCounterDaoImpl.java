package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

public class NotificationsCounterDaoImpl extends HibernateDaoImpl<NotificationsCounter> implements NotificationsCounterDao {
    @Override
    public NotificationsCounter getByUnsubscribeToken(String unsubscribeToken) {
        return (NotificationsCounter) createCriteria()
                .add(Restrictions.eq("unsubscribeToken", unsubscribeToken))
                .uniqueResult();
    }

    @Override
    public NotificationsCounter getByDiver(Diver diver) {
        return (NotificationsCounter) createCriteria()
                .add(Restrictions.eq("diver", diver))
                .uniqueResult();
    }
}
