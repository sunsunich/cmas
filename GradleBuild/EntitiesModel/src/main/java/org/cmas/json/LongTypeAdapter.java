package org.cmas.json;

import com.google.myjson.JsonSyntaxException;
import com.google.myjson.TypeAdapter;
import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonToken;
import com.google.myjson.stream.JsonWriter;
import org.cmas.util.StringUtil;

import java.io.IOException;

/**
 * Created on Jan 24, 2017
 *
 * @author Alexander Petukhov
 */
public class LongTypeAdapter extends TypeAdapter<Long> {

    @Override
    public void write(JsonWriter jsonWriter, Long value)
            throws IOException {
        jsonWriter.value(String.valueOf(value));
    }

    @Override
    public Long read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        try {
            String result = jsonReader.nextString();
            if (StringUtil.isTrimmedEmpty(result)) {
                return null;
            }
            return Long.parseLong(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
