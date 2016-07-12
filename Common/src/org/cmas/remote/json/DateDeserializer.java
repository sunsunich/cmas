package org.cmas.remote.json;

import com.google.myjson.JsonDeserializationContext;
import com.google.myjson.JsonDeserializer;
import com.google.myjson.JsonElement;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private final DateFormat df;

    public DateDeserializer(String dateFormat) {
        df = new SimpleDateFormat(dateFormat);
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        try {
            return df.parse(jsonElement.getAsString());
        } catch (ParseException ignored) {
            return null;
        }
    }
}