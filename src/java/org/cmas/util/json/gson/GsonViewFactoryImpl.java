package org.cmas.util.json.gson;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import org.cmas.Globals;
import org.cmas.util.json.SimpleGsonResponse;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 07/12/12
 * Time: 03:02
 */
public class GsonViewFactoryImpl implements GsonViewFactory {

    @Override
    public GsonView createGsonView(Object toSerialize) {
        Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat(Globals.DTF)
                    .create();
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createSuccessGsonView() {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(true, "");
        return new GsonView(toSerialize, gson);
    }

    @Override
    public GsonView createErrorGsonView(String... message) {
        Gson gson = new Gson();
        SimpleGsonResponse toSerialize = new SimpleGsonResponse(false, message);
        return new GsonView(toSerialize, gson);
    }
}
