package org.cmas.remote.json;

import com.google.myjson.JsonDeserializationContext;
import com.google.myjson.JsonDeserializer;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private DateFormat df;

    public DateDeserializer(String dateFormat) {
        this.df = new SimpleDateFormat(dateFormat);
    }

    @Override
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return df.parse(json.getAsString());
        } catch (ParseException e) {
            return null;
        }
    }
}