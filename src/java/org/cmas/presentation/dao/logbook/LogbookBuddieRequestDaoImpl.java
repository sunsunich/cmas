package org.cmas.presentation.dao.logbook;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
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
public class LogbookBuddieRequestDaoImpl extends HibernateDaoImpl<LogbookBuddieRequest> implements LogbookBuddieRequestDao {

    @Override
    public boolean hasLogbookBuddieRequest(LogbookEntry logbookEntry, Diver from, Diver to) {
        Object result = createSearchCriteria(logbookEntry, from, to)
                .setProjection(Projections.rowCount())
                .uniqueResult();
        return result != null && ((Number) result).intValue() > 0;
    }

    @Override
    public LogbookBuddieRequest getLogbookBuddieRequest(LogbookEntry logbookEntry, Diver from, Diver to) {
        return (LogbookBuddieRequest) createSearchCriteria(logbookEntry, from, to)
                .uniqueResult();
    }

    private Criteria createSearchCriteria(LogbookEntry logbookEntry, Diver from, Diver to) {
        return createCriteria()
                .add(Restrictions.eq("logbookEntry", logbookEntry))
                .add(Restrictions.eq("from", from))
                .add(Restrictions.eq("to", to));
    }

    @Override
    public List<LogbookBuddieRequest> getRequestsToDiver(Diver diver) {
        return createCriteria()
                .add(Restrictions.eq("to", diver))
                .list();
    }
}
