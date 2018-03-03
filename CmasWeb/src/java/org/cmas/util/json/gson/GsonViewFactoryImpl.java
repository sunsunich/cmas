package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.LongSerializationPolicy;
import org.cmas.Globals;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.DateTypeAdapter;
import org.cmas.json.DoubleTypeAdapter;
import org.cmas.json.IntTypeAdapter;
import org.cmas.json.LogbookEntryEditSerializer;
import org.cmas.json.LogbookEntrySerializer;
import org.cmas.json.LongTypeAdapter;
import org.cmas.json.MinimumDiverSerializer;
import org.cmas.json.PersonalCardSerializer;
import org.cmas.json.SimpleGsonResponse;
import org.jetbrains.annotations.NotNull;

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
        return createCommonGsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(LogbookEntry.class, new LogbookEntryEditSerializer())
                .create();
    }

    @Override
    public Gson getPersonalCardsGson() {
        return createCommonGsonBuilder().create();
    }

    @Override
    public GsonView createSocialUpdatesGsonView(Object toSerialize) {
        return new GsonView(toSerialize,  getLogbookEntryEditGson());
    }

    @Override
    public GsonView createGsonView(Object toSerialize) {
        Gson gson = createCommonGsonBuilder()
                    .setDateFormat(Globals.DTF)
                    .registerTypeAdapter(PersonalCard.class, new PersonalCardSerializer())
                    .create();
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createGsonFeedView(List<LogbookEntry> toSerialize) {
        Gson gson = createCommonGsonBuilder()
                .setDateFormat(Globals.DTF)
                .registerTypeAdapter(Diver.class, new MinimumDiverSerializer())
                .registerTypeAdapter(LogbookEntry.class, new LogbookEntrySerializer())
                .create();
        return new GsonView(toSerialize, gson);
    }

    @NotNull
    private static GsonBuilder createCommonGsonBuilder() {
        return new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(Integer.class, new IntTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation();
    }

    @Override
    public GsonView createSuccessGsonView() {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(true, "");
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createErrorGsonView(String message) {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(false, message);
        return new GsonView(toSerialize, gson);
    }
}
