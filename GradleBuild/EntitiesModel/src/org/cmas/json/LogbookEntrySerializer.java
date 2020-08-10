package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookEntry;

import java.lang.reflect.Type;

/**
 * Created on Jul 10, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookEntrySerializer implements JsonSerializer<LogbookEntry> {

    @Override
    public JsonElement serialize(LogbookEntry t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .registerTypeAdapter(Diver.class, new GeneralDiverSerializer())
                                     .setDateFormat(Globals.FEED_DTF)
                                     .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.remove("dateEdit");
        jObj.addProperty("dateEdit", String.valueOf(t.getDateEdit().getTime()));
        Gson minimumDiverGson = CommonGsonCreator.createCommonGsonBuilder()
                                                 .registerTypeAdapter(Diver.class, new MinimumDiverSerializer())
                                                 .create();
        jObj.remove("instructor");
        jObj.add("instructor", minimumDiverGson.toJsonTree(t.getInstructor()));
        jObj.remove("buddies");
        jObj.add("buddies", minimumDiverGson.toJsonTree(t.getBuddies()));
        return jObj;
    }
}
