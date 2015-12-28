package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.reflect.TypeToken;
import org.cmas.Globals;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.EntityEditReply;
import org.cmas.json.logbook.LogbookEntryCreateReply;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteLogbookServiceImpl extends BaseRemoteServiceImpl implements RemoteLogbookService {

    @Override
    public Pair<LogbookEntryCreateReply, String> addNewEntry(
            Context context,
            LogbookEntry logbookEntry
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>(1);
        Gson gson = new GsonBuilder()
             //   .registerTypeAdapter(Document.class, new NewDocumentSerializer())
                .create();
        params.put("logbookEntryJson", gson.toJson(logbookEntry));

        Pair<Pair<LogbookEntryCreateReply, String>, Map<String, String>> result =
                basicPostRequestSend(
                        context, appProperties.getAddNewLogbookEntryURL(), params
                        , LogbookEntryCreateReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<List<LogbookEntry>, String> getDiverLogbookEntries(
            Context context, long maxVersion, long diverId
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("maxVersion", String.valueOf(maxVersion));
        params.put("diverId", String.valueOf(diverId));

        Type resultType = new TypeToken<List<LogbookEntry>>() {
           }.getType();

        Pair<Pair<List<LogbookEntry>, String>, Map<String, String>> result =
                basicGetRequestSend(
                        context, appProperties.getGetDiverLogbookEntriesURL(), params
                        , resultType, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<EntityEditReply, String> editEntry(
            Context context,
            LogbookEntry logbookEntry
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>(1);
        Gson gson = new GsonBuilder()
              //  .registerTypeAdapter(LogbookEntry.class, new EditDocumentSerializer())
                .create();
        params.put("logbookEntryJson", gson.toJson(logbookEntry));

        Pair<Pair<EntityEditReply, String>, Map<String, String>> result =
                basicPostRequestSend(
                        context, appProperties.getEditLogbookEntryURL(), params
                        , EntityEditReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }

    @Override
    public Pair<EntityEditReply, String> deleteEntry(
            Context context,
            final long entryId
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("logbookEntryId", String.valueOf(entryId));

        Pair<Pair<EntityEditReply, String>, Map<String, String>> result =
                basicGetRequestSend(
                        context, appProperties.getDeleteLogbookEntryURL(), params
                        , EntityEditReply.class, Globals.DOCUMENT_DATE_FORMAT
                );
        return result.first;
    }
}
