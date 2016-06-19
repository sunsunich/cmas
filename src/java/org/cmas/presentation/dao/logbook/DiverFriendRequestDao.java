package org.cmas.presentation.dao.logbook;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiverFriendRequestDao extends HibernateDao<DiverFriendRequest> {

    boolean hasDiverFriendRequest(Diver from, Diver to);

    DiverFriendRequest getDiverFriendRequest(Diver from, Diver to);

    List<DiverFriendRequest> getRequestsFromDiver(Diver diver);

    List<DiverFriendRequest> getRequestsToDiver(Diver diver);
}
