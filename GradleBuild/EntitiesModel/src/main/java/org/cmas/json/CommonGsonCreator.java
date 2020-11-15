package org.cmas.json;

import com.google.myjson.GsonBuilder;
import com.google.myjson.LongSerializationPolicy;

public final class CommonGsonCreator {

    private CommonGsonCreator() {
    }

    public static GsonBuilder createCommonGsonBuilder() {
        return new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapter(Integer.class, new IntTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation();
    }
}
