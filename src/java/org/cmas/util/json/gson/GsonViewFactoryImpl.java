package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.LongSerializationPolicy;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.LogbookEntrySerializer;
import org.cmas.json.MinimumDiverSerializer;
import org.cmas.json.SimpleGsonResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 07/12/12
 * Time: 03:02
 */
public class GsonViewFactoryImpl implements GsonViewFactory {

    @Override
    public GsonView createGsonView(Object toSerialize) {
        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat(Globals.DTF)
                    .create();
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createGsonFeedView(List<LogbookEntry> toSerialize) {
        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .registerTypeAdapter(Diver.class, new MinimumDiverSerializer())
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
    public GsonView createErrorGsonView(String message) {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(false, message);
        return new GsonView(toSerialize, gson);
    }
}
