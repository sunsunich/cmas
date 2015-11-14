package org.cmas.presentation.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.validation.BindingResult;
import org.cmas.presentation.model.Transferable;

public interface EntityAddService<F extends Transferable<E>,E> {

    @Nullable
    E add(F formObject, BindingResult result);
}
