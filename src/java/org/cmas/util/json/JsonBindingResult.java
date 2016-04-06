package org.cmas.util.json;

import org.cmas.json.JsonBindingResultModel;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class JsonBindingResult extends JsonBindingResultModel {

    public JsonBindingResult(Errors bindingResult) {
        this(bindingResult, true);
    }

    public JsonBindingResult(Errors bindingResult, boolean isUseCodes) {
        if (bindingResult.hasErrors()) {
            for (Object error : bindingResult.getFieldErrors()) {
                FieldError fieldError = (FieldError) error;
                String message = isUseCodes ? fieldError.getCode() : fieldError.getDefaultMessage();
                fieldErrors.put(fieldError.getField(), message);
            }
            for (Object error : bindingResult.getGlobalErrors()) {
                ObjectError objectError = (ObjectError) error;
                String message = isUseCodes ? objectError.getCode() : objectError.getDefaultMessage();
                errors.add(message);
            }
        }
        else{
            success = true;
        }
    }
}
