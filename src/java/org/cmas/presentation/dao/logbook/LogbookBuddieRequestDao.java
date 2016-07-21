package org.cmas.presentation.dao.logbook;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
public interface LogbookBuddieRequestDao extends HibernateDao<LogbookBuddieRequest> {

    boolean hasLogbookBuddieRequest(LogbookEntry logbookEntry, Diver from, Diver to);

    LogbookBuddieRequest getLogbookBuddieRequest(LogbookEntry logbookEntry,Diver from, Diver to);

    List<LogbookBuddieRequest> getRequestsToDiver(Diver diver);
}
