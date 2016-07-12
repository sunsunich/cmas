package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import com.google.myjson.LongSerializationPolicy;
import org.cmas.Globals;
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
        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.remove("dateEdit");
        jObj.addProperty("dateEdit", t.getDateEdit().getTime());
        return jObj;
    }
}