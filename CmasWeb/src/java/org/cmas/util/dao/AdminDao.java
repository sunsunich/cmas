package org.cmas.util.dao;

import org.cmas.presentation.model.SortPaginator;

import java.util.List;

public interface AdminDao<T, S extends SortPaginator>{

    List<T> adminSearch(S form);

    int getMaxCountAdminSearch(S form);
}
