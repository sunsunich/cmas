package org.cmas.presentation.model;

public class CursorData implements SortLimitInfo {

    // направление сортировки
    private SortDirection dir;
    // столбец по которому сортируем
    private String sort;
    // количество записей в выдаче
    private int limit;
    // с какой записи начинать
    private int start;

    @Override
    public SortDirection getDir() {
        return dir;
    }

    public void setDir(SortDirection dir) {
        this.dir = dir;
    }

    @Override
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
