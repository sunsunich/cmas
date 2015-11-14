package org.cmas.presentation.model;


public interface Transferable<E> {
    
    void transferToEntity(E entity);
    void transferFromEntity(E entity);
}
