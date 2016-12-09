package org.cmas.presentation.service;

import org.cmas.presentation.model.Transferable;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.BindingResult;

public interface EntityAddService<F extends Transferable<E>,E> {

    @Nullable
    E add(F formObject, BindingResult result);
}
