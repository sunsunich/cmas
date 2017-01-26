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
    public void write(JsonWriter out, Long value)
            throws IOException {
        out.value(String.valueOf(value));
    }

    @Override
    public Long read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if (StringUtil.isTrimmedEmpty(result)) {
                return null;
            }
            return Long.parseLong(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
