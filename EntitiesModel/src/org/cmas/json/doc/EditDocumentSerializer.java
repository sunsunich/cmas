package org.cmas.json.doc;

import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import org.cmas.entities.doc.Document;

import java.lang.reflect.Type;

public class EditDocumentSerializer extends DocumentSerializer {

    @Override
    public JsonElement serialize(Document obj, Type type, JsonSerializationContext jsc) {
        JsonElement jsonElement = super.serialize(obj, type, jsc);
        JsonObject jObj = (JsonObject) jsonElement;
        jObj.remove("version");
        jObj.remove("creatorId");
        jObj.remove("authorId");
        jObj.remove("dateCreation");
        jObj.remove("dateSigned");
        jObj.remove("digest");
        jObj.remove("typeId");
        jObj.remove("directiveId");
        jObj.remove("customFields");
        return jObj;
    }
}
