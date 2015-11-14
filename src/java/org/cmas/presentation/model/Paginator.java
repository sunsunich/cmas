package org.cmas.presentation.model;


public interface Paginator {
    /**
     * По сколько записей выводим?
     * @return
     */
    int getLimit();

    /**
     * с какой записи выводим?
     * @return
     */
    int getOffset();

    void setLimit(int limit);
}
