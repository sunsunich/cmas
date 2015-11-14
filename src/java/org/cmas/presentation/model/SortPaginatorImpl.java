package org.cmas.presentation.model;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;


public abstract class SortPaginatorImpl<E extends Enum<E>> extends PaginatorImpl implements SortPaginator{

    // направление сортировки
    private boolean dir;

    //колонка, по которой будем сортировать
    @NotNull
    private E sortColumn;

    //это поле нужно, чтобы jsp нормально работал
    @Nullable
    private String sort;

    protected abstract Class<E> getEnumClass();

    protected SortPaginatorImpl(@NotNull E sortColumn) {
        this.sortColumn = sortColumn;
    }

    protected SortPaginatorImpl(int limit, int offset, @NotNull E sortColumn) {
        super(limit, offset);
        this.sortColumn = sortColumn;
    }

    @NotNull
    public E getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(@NotNull E sortColumn) {
        this.sortColumn = sortColumn;
    }

    @Override
    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public void setSort(@Nullable String sort) {
        this.sort = sort;
        if(sort != null && !sort.isEmpty()){
            sortColumn = Enum.valueOf(getEnumClass(),sort);
        }
    }

    @Nullable
    public String getSort() {
        return sort;
    }

    @Override
    public String getSortColumnName() {
        if (sortColumn instanceof ColumnName) {
            return ((ColumnName)sortColumn).getName();
        } else {
            return sortColumn.name();
        }

    }
}
