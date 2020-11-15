package org.cmas.json;

import com.google.myjson.JsonSyntaxException;
import com.google.myjson.TypeAdapter;
import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonToken;
import com.google.myjson.stream.JsonWriter;
import org.cmas.Globals;
import org.cmas.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created on Jan 24, 2017
 *
 * @author Alexander Petukhov
 */
public class DateTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value)
            throws IOException {
        String formattedValue = value == null ? null : Globals.getDTFWebControls().format(value);
        out.value(formattedValue);
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String result = in.nextString();
            if (StringUtil.isTrimmedEmpty(result)) {
                return null;
            }
            result = StringUtil.correctSpaceCharAndTrim(result);
            if (result.length() <= 11) {
                return Globals.getDTF().parse(result);
            }
            return Globals.getDTFWebControls().parse(result);
        } catch (ParseException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
