package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.CommonGsonCreator;
import org.cmas.json.DateTypeAdapter;
import org.cmas.json.GeneralDiverSerializer;
import org.cmas.json.LogbookEntryEditSerializer;
import org.cmas.json.LogbookEntrySerializer;
import org.cmas.json.PersonalCardSerializer;
import org.cmas.json.SimpleGsonResponse;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 07/12/12
 * Time: 03:02
 */
public class GsonViewFactoryImpl implements GsonViewFactory {

    @Override
    public Gson getLogbookEntryEditGson() {
        return CommonGsonCreator.createCommonGsonBuilder()
                                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                                .registerTypeAdapter(Diver.class, new GeneralDiverSerializer())
                                .registerTypeAdapter(LogbookEntry.class, new LogbookEntryEditSerializer())
                                .create();
    }

    @Override
    public Gson getCommonGson() {
        return CommonGsonCreator.createCommonGsonBuilder()
                                .setDateFormat(Globals.DTF)
                                .registerTypeAdapter(PersonalCard.class, new PersonalCardSerializer())
                                .registerTypeAdapter(Diver.class, new GeneralDiverSerializer())
                                .create();
    }

    @Override
    public GsonView createSocialUpdatesGsonView(Object toSerialize) {
        return createCommonDiverView(toSerialize);
    }

    @Override
    public GsonView createGsonView(Object toSerialize) {
        Gson gson = getCommonGson();
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createDiverView(List<Diver> divers) {
        return createCommonDiverView(divers);
    }

    @Override
    public GsonView createDiverFriendRequestView(List<DiverFriendRequest> friendRequests) {
        return createCommonDiverView(friendRequests);
    }

    @Override
    public GsonView createDiverView(Diver diver) {
        return createCommonDiverView(diver);
    }

    @Override
    public GsonView createSecureDiverView(Diver diver) {
        return new GsonView(diver,
                            CommonGsonCreator.createCommonGsonBuilder()
                                             .registerTypeAdapter(PersonalCard.class, new PersonalCardSerializer())
                                             .create()
        );
    }

    private GsonView createCommonDiverView(Object toSerialize) {
//        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
//                                     .registerTypeAdapter(Diver.class, new GeneralDiverSerializer())
//                                     .create();
        return new GsonView(toSerialize, getCommonGson());
    }

    @Override
    public GsonView createGsonFeedView(List<LogbookEntry> toSerialize) {
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .registerTypeAdapter(LogbookEntry.class, new LogbookEntrySerializer())
                                     .create();
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createSuccessGsonView() {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(true, "");
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createDiverVerificationGsonView(List<Diver> divers) {
        return createCommonDiverView(divers);
//        .registerTypeAdapter(Diver.class, new DiverVerificationSerializer())
    }

    @Override
    public GsonView createErrorGsonView(String message) {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(false, message);
        return new GsonView(toSerialize, gson);
    }
}
