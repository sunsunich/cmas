package org.cmas.json;

import com.google.myjson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBindingResultModel {

    @Expose
    protected boolean success;
    @Expose
    protected final List<String> errors;
    @Expose
    protected final Map<String, String> fieldErrors;

    public JsonBindingResultModel() {
        success = false;
        errors = new ArrayList<>();
        fieldErrors = new HashMap<>();
    }

    public boolean getSuccess() {
        return success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
