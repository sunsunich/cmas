package org.cmas.json.doc;

import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import org.cmas.entities.doc.Document;

import java.lang.reflect.Type;

public class NewDocumentSerializer extends DocumentSerializer {

    @Override
    public JsonElement serialize(Document obj, Type type, JsonSerializationContext jsc) {
        JsonElement jsonElement = super.serialize(obj, type, jsc);
        JsonObject jObj = (JsonObject) jsonElement;
        jObj.remove("id");
        jObj.remove("version");
        jObj.remove("authorId");
        jObj.remove("dateCreation");
        jObj.remove("dateSigned");
        jObj.remove("digest");
        jObj.remove("directiveId");
        jObj.remove("customFields");
        /*
        "creatorId":4,
              "date":"2013-10-16",
            	"typeId":4,
            	"name":"Документ 5",
            	"description":"Описание",
        "files":[[file=>'base64_string', ext=>'pdf]', [file=>'base64_string', ext=>'pdf]],

        // только для документа типа Госпитализация
        “dateHospitalizationFrom”:”2013-10-20”,
        “dateHospitalizationTill”:”2013-10-27”
         */
        return jObj;
    }
}
