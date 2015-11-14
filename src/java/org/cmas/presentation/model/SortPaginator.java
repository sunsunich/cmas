package org.cmas.presentation.model;


public interface SortPaginator extends Paginator{

    boolean isDir();

    String getSortColumnName();
}
