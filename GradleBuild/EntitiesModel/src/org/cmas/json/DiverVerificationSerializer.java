package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import com.google.myjson.LongSerializationPolicy;
import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;

import java.lang.reflect.Type;

/**
 * Created on Jul 10, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverVerificationSerializer implements JsonSerializer<Diver> {

    @Override
    public JsonElement serialize(Diver t, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(Integer.class, new IntTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.SHORT_DATE_FORMAT)
                .registerTypeAdapter(PersonalCard.class, new PersonalCardSerializer())
                .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.remove("isAddFriendsToLogbookEntries");
        jObj.remove("isNewsFromCurrentLocation");
        jObj.remove("payedAtLeastOnce");
        jObj.remove("id");
        jObj.remove("email");
        jObj.remove("phone");
        jObj.remove("dateLicencePaymentIsDue");
        return jObj;
    }
}
