package org.cmas.presentation.model;

import org.jetbrains.annotations.Nullable;


public interface Editable<E> extends Transferable<E>{
    @Nullable
    Long getId();
}
