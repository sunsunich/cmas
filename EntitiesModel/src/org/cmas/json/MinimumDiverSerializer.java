package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;

import java.lang.reflect.Type;

/**
 * Created on Jul 10, 2016
 *
 * @author Alexander Petukhov
 */
public class MinimumDiverSerializer implements JsonSerializer<Diver> {

    @Override
    public JsonElement serialize(Diver t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.entrySet().clear();
        jObj.addProperty("firstName", t.getFirstName());
        jObj.addProperty("lastName", t.getLastName());
        jObj.addProperty("photo", t.getPhoto());
        jObj.addProperty("id", t.getId());
        return jObj;
    }
}
