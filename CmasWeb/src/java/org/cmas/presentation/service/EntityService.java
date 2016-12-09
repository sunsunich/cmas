package org.cmas.presentation.service;

import org.cmas.presentation.model.Editable;

public interface EntityService<F extends Editable<E>,E>
        extends EntityDeleteService, EntityAddService<F,E>, EntityEditService<F,E>{


}
