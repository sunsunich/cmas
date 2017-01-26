package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import org.cmas.entities.logbook.LogbookEntry;

import java.util.List;

public interface GsonViewFactory {

    GsonView createSocialUpdatesGsonView(Object toSerialize);

    GsonView createGsonView(Object toSerialize);

    Gson getLogbookEntryEditGson();

    GsonView createGsonFeedView(List<LogbookEntry> toSerialize);

    GsonView createSuccessGsonView();

    GsonView createErrorGsonView(String message);
}
