package org.cmas.util.json.gson;

import org.cmas.entities.logbook.LogbookEntry;

import java.util.List;

public interface GsonViewFactory {

    GsonView createGsonView(Object toSerialize);

    GsonView createGsonFeedView(List<LogbookEntry> toSerialize);

    GsonView createSuccessGsonView();

    GsonView createErrorGsonView(String message);
}
