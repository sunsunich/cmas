package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
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
        Gson gson = CommonGsonCreator.createCommonGsonBuilder().create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.entrySet().clear();
        jObj.addProperty("id", String.valueOf(t.getId()));
        jObj.addProperty("firstName", t.getFirstName());
        jObj.addProperty("lastName", t.getLastName());
        jObj.addProperty("userpicUrl", t.getUserpicUrl());
        return jObj;
    }
}
