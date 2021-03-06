package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.entities.DictionaryEntity;

import java.lang.reflect.Type;

public class DeletableEntitiesSerializer implements JsonSerializer<DictionaryEntity> {

    @Override
    public JsonElement serialize(DictionaryEntity t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .setDateFormat(Globals.DTF)
                                     .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        handleDeletableEntitySerialization(t, jObj);
        return jObj;
    }

    public static void handleDeletableEntitySerialization(DictionaryEntity obj, JsonObject jObj) {
        if (obj.isDeleted()) {
            jObj.entrySet().clear();
            jObj.addProperty("deleted", true);
            jObj.addProperty("version", obj.getVersion());
            jObj.addProperty("id", obj.getId());
        } else {
            jObj.remove("deleted");
        }
    }
}
