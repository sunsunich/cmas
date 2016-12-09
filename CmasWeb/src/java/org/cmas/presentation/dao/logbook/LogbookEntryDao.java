package org.cmas.presentation.dao.logbook;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.DictionaryDataDao;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;

import java.util.Collection;
import java.util.List;

/**
 * Created on Jul 02, 2016
 *
 * @author Alexander Petukhov
 */
public interface LogbookEntryDao extends DictionaryDataDao<LogbookEntry> {

    List<LogbookEntry> getDiverLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject);

    List<LogbookEntry> getDiverFriendsLogbookFeed(Diver diver, SearchLogbookEntryFormObject formObject);

    List<LogbookEntry> getDiverPublicLogbookFeed(Diver diver, Collection<Country> countries, SearchLogbookEntryFormObject formObject);

    List<LogbookEntry> getPublicLogbookFeed(Collection<Country> countries, SearchLogbookEntryFormObject formObject);
}
