package org.cmas.presentation.model;


public class PaginatorImpl implements Paginator {

    // количество записей в выдаче
    protected int limit;
    // с какой записи начинать
    protected int offset;

    public PaginatorImpl() {
    }

    public PaginatorImpl(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
