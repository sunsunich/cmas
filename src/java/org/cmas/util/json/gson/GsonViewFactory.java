package org.cmas.util.json.gson;

public interface GsonViewFactory {

    GsonView createGsonView(Object toSerialize);

    GsonView createSuccessGsonView();

    GsonView createErrorGsonView(String message);
}
