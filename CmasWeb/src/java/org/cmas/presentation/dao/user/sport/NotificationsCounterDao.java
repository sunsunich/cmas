package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.util.dao.HibernateDao;

public interface NotificationsCounterDao extends HibernateDao<NotificationsCounter> {

    NotificationsCounter getByUnsubscribeToken(String unsubscribeToken);

    NotificationsCounter getByDiver(Diver diver);
}
