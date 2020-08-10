package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookEntry;

import java.util.List;

public interface GsonViewFactory {

    Gson getCommonGson();

    GsonView createSocialUpdatesGsonView(Object toSerialize);

    GsonView createGsonView(Object toSerialize);

    GsonView createDiverView(List<Diver> divers);

    GsonView createDiverFriendRequestView(List<DiverFriendRequest> friendRequests);

    GsonView createDiverView(Diver diver);

    Gson getLogbookEntryEditGson();

    GsonView createGsonFeedView(List<LogbookEntry> toSerialize);

    GsonView createSuccessGsonView();

    GsonView createDiverVerificationGsonView(List<Diver> divers);

    GsonView createErrorGsonView(String message);
}
