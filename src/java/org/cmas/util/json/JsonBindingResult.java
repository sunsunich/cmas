package org.cmas.util.json;

import com.google.myjson.annotations.Expose;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBindingResult {

    @Expose
    private boolean success;
    @Expose
    private final List<String> errors;
    @Expose
    private final Map<String, String> fieldErrors;

    public JsonBindingResult() {
        success = false;        
        errors = new ArrayList<String>();
        fieldErrors = new HashMap<String, String>();
    }

    public JsonBindingResult(BindingResult bindingResult) {
        this();
        if (bindingResult.hasErrors()) {
            for (Object error : bindingResult.getFieldErrors()) {
                FieldError fieldError = (FieldError) error;
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            for (Object error : bindingResult.getGlobalErrors()) {
                ObjectError objectError = (ObjectError) error;
                errors.add(objectError.getDefaultMessage());
            }
        }
        else{
            success = true;
        }
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
