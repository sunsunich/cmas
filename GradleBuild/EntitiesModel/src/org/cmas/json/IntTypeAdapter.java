package org.cmas.json;

import com.google.myjson.JsonSyntaxException;
import com.google.myjson.TypeAdapter;
import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonToken;
import com.google.myjson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created on Jan 24, 2017
 *
 * @author Alexander Petukhov
 */
public class IntTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value)
            throws IOException {
        out.value(value);
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return null;
            }
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
