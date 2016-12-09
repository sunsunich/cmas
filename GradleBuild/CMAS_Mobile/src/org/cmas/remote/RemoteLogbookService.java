package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.EntityEditReply;
import org.cmas.json.logbook.LogbookEntryCreateReply;

import java.util.List;

public interface RemoteLogbookService extends BaseRemoteService {

    Pair<LogbookEntryCreateReply, String> addNewEntry(
            Context context,
            LogbookEntry logbookEntry
    ) throws Exception;

    Pair<EntityEditReply, String> editEntry(
            Context context,
            LogbookEntry logbookEntry
    ) throws Exception;

    Pair<EntityEditReply, String> deleteEntry(
            Context context,
            long entryId
    ) throws Exception;

    Pair<List<LogbookEntry>, String> getDiverLogbookEntries(
            Context context,
            long maxVersion,
            long diverId
    ) throws Exception;
}
