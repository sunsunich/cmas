package org.cmas.presentation.dao.logbook;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookEntryState;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;
import org.cmas.util.StringUtil;
import org.hibernate.Query;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diver d"
                     + " inner join le.diveSpec spec"
                     + " left outer join le.diveSpot spot"
                     + " left outer join spot.country country"
                     + " where d.id = :diverId"
                     + " and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, false, false);
        return query.setLong("diverId", diver.getId()).list();
    }

    @Override
    public List<LogbookEntry> getFriendsOnlyLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diver d"
                     + " left outer join d.friends f"
                     + " left outer join d.friendOf fof"
                     + " where (f.id = :diverId and le.visibility != :visibility"
                     + " or fof.id = :diverId and le.visibility != :visibility)"
                     + " and le.deleted = :deleted"
                     + " and le.state = :state";
        Query query = addSearchFormObjectToQuery(formObject, hql, true, true);
        return query.setLong("diverId", diver.getId()).list();
    }

    @Override
    public List<LogbookEntry> getDiverFriendsLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diver d"
                     + " left outer join d.friends f"
                     + " left outer join d.friendOf fof"
                     + " where (d.id = :diverId"
                     + " or f.id = :diverId and le.visibility != :visibility and le.state = :state"
                     + " or fof.id = :diverId and le.visibility != :visibility and le.state = :state)"
                     + " and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, true, true);
        return query.setLong("diverId", diver.getId()).list();
    }

    @Override
    public List<LogbookEntry> getDiverPublicLogbookFeed(Diver diver, Collection<Country> countries, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diver d"
                     + " left outer join le.diveSpot spot"
                     + " left outer join spot.country country"
                     + " left outer join d.friends f"
                     + " left outer join d.friendOf fof"
                     + " where (d.id = :diverId"
                     + " or f.id = :diverId and le.visibility != :visibility and le.state = :state"
                     + " or fof.id = :diverId and le.visibility != :visibility and le.state = :state"
                     + " or le.visibility = :pubVis and le.state = :state)";
        if (countries != null) {
            hql += " and country.id in (:countryIds)";
        }
        hql += " and le.deleted = :deleted";
        Query query = addSearchFormObjectToQuery(formObject, hql, true, true);
        query.setParameter("pubVis", LogbookVisibility.PUBLIC);
        addCountriesToQuery(countries, query);
        return query.setLong("diverId", diver.getId()).list();
    }

    private static void addCountriesToQuery(Collection<Country> countries, Query query) {
        if (countries != null && !countries.isEmpty()) {
            Collection<Long> countryIds = new HashSet<>();
            for (Country country : countries) {
                countryIds.add(country.getId());
            }
            query.setParameterList("countryIds", countryIds);
        }
    }

    @Override
    public List<LogbookEntry> getPublicLogbookFeed(Collection<Country> countries, SearchLogbookEntryFormObject formObject) {
        String hql = "select distinct le from org.cmas.entities.logbook.LogbookEntry le"
                     + " left outer join le.diveSpot spot"
                     + " left outer join spot.country country"
                     + " where le.visibility = :pubVis";
        if (countries != null) {
            hql += " and country.id in (:countryIds)";
        }
        hql += " and le.deleted = :deleted"
               + " and le.state = :state";
        Query query = addSearchFormObjectToQuery(formObject, hql, false, true);
        query.setParameter("pubVis", LogbookVisibility.PUBLIC);
        addCountriesToQuery(countries, query);
        return query.list();
    }

    private Query addSearchFormObjectToQuery(SearchLogbookEntryFormObject formObject, String hql, boolean hasVisibility, boolean hasState) {
        String fromDateTimestamp = null;
        String toDateTimestamp = null;
        String fromDate = formObject.getFromDate();
        String toDate = formObject.getToDate();
        if (StringUtil.isTrimmedEmpty(fromDate)
            && StringUtil.isTrimmedEmpty(toDate)) {
            fromDateTimestamp = formObject.getFromDateTimestamp();
            if (!StringUtil.isTrimmedEmpty(fromDateTimestamp)) {
                hql += " and le.dateEdit > :fromDate";
            }
            toDateTimestamp = formObject.getToDateTimestamp();
            if (!StringUtil.isTrimmedEmpty(toDateTimestamp)) {
                hql += " and le.dateEdit < :toDate";
            }
        } else {
            if (!StringUtil.isTrimmedEmpty(fromDate)) {
                hql += " and le.dateEdit >= :fromDate";
            }
            if (!StringUtil.isTrimmedEmpty(toDate)) {
                hql += " and le.dateEdit <= :toDate";
            }
        }
        String countryCode = formObject.getCountry();
        if (!StringUtil.isTrimmedEmpty(countryCode)) {
            hql += " and country.code = :code";
        }
        String fromMeters = formObject.getFromMeters();
        if (!StringUtil.isTrimmedEmpty(fromMeters)) {
            hql += " and spec.maxDepthMeters >= :fromMeters";
        }
        String toMeters = formObject.getToMeters();
        if (!StringUtil.isTrimmedEmpty(toMeters)) {
            hql += " and spec.maxDepthMeters <= :toMeters";
        }
        String fromMinutes = formObject.getFromMinutes();
        if (!StringUtil.isTrimmedEmpty(fromMinutes)) {
            hql += " and spec.durationMinutes >= :fromMinutes";
        }
        String toMinutes = formObject.getToMinutes();
        if (!StringUtil.isTrimmedEmpty(toMinutes)) {
            hql += " and spec.durationMinutes <= :toMinutes";
        }
        hql += " order by le.dateEdit desc";
        Query query = createQuery(hql);
        if (hasVisibility) {
            query.setParameter("visibility", LogbookVisibility.PRIVATE);
        }
        query.setBoolean("deleted", false);
        if (hasState) {
            query.setParameter("state", LogbookEntryState.PUBLISHED);
        }
        if (!StringUtil.isTrimmedEmpty(fromDateTimestamp)) {
            query.setTimestamp("fromDate", new Date(Long.parseLong(fromDateTimestamp)));
        }
        if (!StringUtil.isTrimmedEmpty(toDateTimestamp)) {
            query.setTimestamp("toDate", new Date(Long.parseLong(toDateTimestamp)));
        }
        if (!StringUtil.isTrimmedEmpty(countryCode)) {
            query.setString("code", StringUtil.correctSpaceCharAndTrim(countryCode));
        }
        if (!StringUtil.isTrimmedEmpty(fromMeters)) {
            query.setInteger("fromMeters", Integer.parseInt(fromMeters));
        }
        if (!StringUtil.isTrimmedEmpty(toMeters)) {
            query.setInteger("toMeters", Integer.parseInt(toMeters));
        }
        if (!StringUtil.isTrimmedEmpty(fromMinutes)) {
            query.setInteger("fromMinutes", Integer.parseInt(fromMinutes));
        }
        if (!StringUtil.isTrimmedEmpty(toMinutes)) {
            query.setInteger("toMinutes", Integer.parseInt(toMinutes));
        }
        try {
            if (!StringUtil.isTrimmedEmpty(fromDate)) {
                query.setTimestamp("fromDate", Globals.getDTF().parse(fromDate));
            }
            if (!StringUtil.isTrimmedEmpty(toDate)) {
                query.setTimestamp("toDate", Globals.getDTF().parse(toDate));
            }
        } catch (ParseException ignored) {
        }
        String limit = formObject.getLimit();
        if (!StringUtil.isTrimmedEmpty(limit)) {
            query.setMaxResults(Integer.parseInt(limit));
        }
        return query;
    }
}
