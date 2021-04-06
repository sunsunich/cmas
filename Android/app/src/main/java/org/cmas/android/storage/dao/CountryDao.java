package org.cmas.android.storage.dao;

import androidx.room.Dao;
import androidx.room.Query;
import org.cmas.android.storage.entities.Country;

import java.util.List;

@Dao
public abstract class CountryDao extends DictionaryEntityDao<Country> {

    @Query("SELECT * FROM COUNTRY WHERE ID = :id")
    public abstract Country getById(long id);

    @Query("SELECT MAX(VERSION) FROM COUNTRY")
    public abstract long getMaxVersion();

    @Query("SELECT * FROM COUNTRY ORDER BY NAME ASC")
    public abstract List<Country> getAllSortedAlphabetically();
}
