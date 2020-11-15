package org.cmas.json;

import com.google.myjson.Gson;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
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
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .setDateFormat(Globals.DOB_DTF)
                                     .registerTypeAdapter(PersonalCard.class, new PersonalCardSerializer())
                                     .create();
        JsonObject jObj = (JsonObject) gson.toJsonTree(t);
        jObj.remove("isAddFriendsToLogbookEntries");
        jObj.remove("isNewsFromCurrentLocation");
        jObj.remove("payedAtLeastOnce");
        jObj.remove("id");
        jObj.remove("email");
        jObj.remove("phone");
        jObj.remove("dob");
        jObj.remove("dateLicencePaymentIsDue");
        return jObj;
    }
}
