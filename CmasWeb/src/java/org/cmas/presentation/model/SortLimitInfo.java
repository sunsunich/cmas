package org.cmas.presentation.model;


public interface SortLimitInfo {
    
    SortDirection getDir();
    String getSort();
    int getLimit();
    int getStart();

}
