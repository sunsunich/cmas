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
public class LogbookEntryEditSerializer implements JsonSerializer<LogbookEntry> {

    @Override
    public JsonElement serialize(LogbookEntry t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .setDateFormat(Globals.DTF_WEB_CONTROLS)
                                     .registerTypeAdapter(Diver.class, new GeneralDiverSerializer())
                                     .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.remove("dateEdit");
        jObj.remove("dateCreation");
        jObj.remove("diver");
        return jObj;
    }
}
