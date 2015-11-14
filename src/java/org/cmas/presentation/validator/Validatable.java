package org.cmas.presentation.validator;

import org.springframework.validation.Errors;


public interface Validatable {    

    void validate(Errors errors);
}
