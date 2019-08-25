package org.cmas.presentation.model;


public interface Paginator {

    int getLimit();

    int getOffset();

    void setLimit(int limit);
}
