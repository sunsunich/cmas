package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCard;

import java.lang.reflect.Type;

/**
 * Created on Jul 10, 2016
 *
 * @author Alexander Petukhov
 */
public class PersonalCardSerializer implements JsonSerializer<PersonalCard> {

    @Override
    public JsonElement serialize(PersonalCard t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DTF)
                .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.addProperty("printName", t.getPrintName());
        jObj.addProperty("printNumber", t.getPrintNumber());
        return jObj;
    }

}
