package org.cmas.presentation.service;

import org.cmas.presentation.model.Editable;
import org.springframework.validation.BindingResult;

public interface EntityEditService<F extends Editable<E>,E> {

    void edit(F formObject, BindingResult result);
}
