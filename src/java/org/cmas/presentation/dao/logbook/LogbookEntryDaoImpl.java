package org.cmas.presentation.dao.logbook;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.SearchDaoUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created on Jul 02, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings({"unchecked", "StringConcatenation"})
public class LogbookEntryDaoImpl extends DictionaryDataDaoImpl<LogbookEntry> implements LogbookEntryDao {

    @Override
    public List<LogbookEntry> getDiverLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject) {
        Criteria criteria = createNotDeletedCriteria().add(Restrictions.eq("diver", diver));
        SearchDaoUtils.addStrictTimestampRangeToSearchCriteria(
                criteria, formObject.getFromDateTimestamp(), formObject.getToDateTimestamp(), "dateEdit"
        );
        criteria.addOrder(SearchDaoUtils.getOrder("dateEdit", true));
        String limit = formObject.getLimit();
        if (!StringUtil.isTrimmedEmpty(limit)) {
            criteria.setMaxResults(Integer.parseInt(limit));
        }
        return criteria.list();
    }

    @Override
    public List<LogbookEntry> getDiverFriendsLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diver d"
                     + " inner join d.friends f"
                     + " where (d.id = :diverId or f.id = :diverId and le.visibility != :visibility) and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, true);
        return query.setLong("diverId", diver.getId()).list();
    }

    @Override
    public List<LogbookEntry> getDiverPublicLogbookFeed(Diver diver, Collection<Country> countries, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diveSpot ds"
                     + " inner join ds.country country"
                     + " inner join le.diver d"
                     + " inner join d.friends f"
                     + " where (d.id = :diverId"
                     + " or f.id = :diverId and le.visibility != :visibility "
                     + " or le.visibility = :pubVis)";
        if (countries != null) {
            hql += " and country in (:countries)";
        }
        hql += " and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, true);
        query.setParameter("pubVis", LogbookVisibility.PUBLIC);
        if (countries != null) {
            query.setParameter("countries", countries);
        }
        return query.setLong("diverId", diver.getId()).list();
    }

    @Override
    public List<LogbookEntry> getPublicLogbookFeed(Collection<Country> countries, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diveSpot ds"
                     + " inner join ds.country country"
                     + " where le.visibility = :pubVis";
        if (countries != null) {
            hql += " and country in (:countries)";
        }
        hql += " and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, false);
        query.setParameter("pubVis", LogbookVisibility.PUBLIC);
        if (countries != null) {
            query.setParameter("countries", countries);
        }
        return query.list();
    }

    private Query addSearchFormObjectToQuery(SearchLogbookEntryFormObject formObject, String hql, boolean hasVisibility) {
        String fromDate = formObject.getFromDateTimestamp();
        if (!StringUtil.isTrimmedEmpty(fromDate)) {
            hql += " and le.dateEdit > :fromDate";
        }
        String toDate = formObject.getToDateTimestamp();
        if (!StringUtil.isTrimmedEmpty(toDate)) {
            hql += " and le.dateEdit < :toDate";
        }
        hql += " order by le.dateEdit desc";
        Query query = createQuery(hql);
        if (hasVisibility) {
            query.setParameter("visibility", LogbookVisibility.PRIVATE);
        }
        query.setBoolean("deleted", false);
        if (!StringUtil.isTrimmedEmpty(fromDate)) {
            query.setTimestamp("fromDate", new Date(Long.parseLong(fromDate)));
        }
        if (!StringUtil.isTrimmedEmpty(toDate)) {
            query.setTimestamp("toDate", new Date(Long.parseLong(toDate)));
        }
        String limit = formObject.getLimit();
        if (!StringUtil.isTrimmedEmpty(limit)) {
            query.setMaxResults(Integer.parseInt(limit));
        }
        return query;
    }
}
