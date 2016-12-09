package org.cmas.service.logbook;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.EntityEditReply;
import org.cmas.json.logbook.LogbookEntryCreateReply;

import java.util.List;

public interface LogbookService {

    void loadLogbook(Context context, long diverId);

    List<LogbookEntry> getByDiverNoRemoteCall(Context context, long diverId, String name);

    Pair<LogbookEntryCreateReply, String> addNewEntry(Activity activity,
                                             LogbookEntry logbookEntry
    );

    Pair<EntityEditReply, String> editEntry(
            Context context,
            LogbookEntry logbookEntry
    );

    Pair<EntityEditReply, String> deleteEntry(
            Context context,
            long entryId
    );
}
