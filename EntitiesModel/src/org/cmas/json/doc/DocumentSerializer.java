package org.cmas.json.doc;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.entities.doc.Document;
import org.cmas.json.DeletableEntitiesSerializer;

import java.lang.reflect.Type;

public class DocumentSerializer implements JsonSerializer<Document> {

    @Override
    public JsonElement serialize(Document obj, Type type, JsonSerializationContext jsc) {
        Gson gson = createDocGSON();
        JsonObject jObj = (JsonObject) gson.toJsonTree(obj);
        DeletableEntitiesSerializer.handleDeletableEntitySerialization(obj, jObj);
        return jObj;
    }

    public static Gson createDocGSON() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(Globals.DOCUMENT_DATE_FORMAT)
                .create();
    }
}
