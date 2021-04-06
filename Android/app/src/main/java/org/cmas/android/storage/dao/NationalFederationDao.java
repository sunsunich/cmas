package org.cmas.android.storage.dao;

import androidx.room.Dao;
import androidx.room.Query;
import org.cmas.android.storage.entities.sport.NationalFederation;

import java.util.List;

@Dao
public abstract class NationalFederationDao extends DictionaryEntityDao<NationalFederation> {

    @Query("SELECT * FROM NATIONAL_FEDERATION WHERE ID = :id")
    public abstract NationalFederation getById(long id);

    @Query("SELECT MAX(VERSION) FROM NATIONAL_FEDERATION")
    public abstract long getMaxVersion();

    @Query("SELECT * FROM NATIONAL_FEDERATION ORDER BY NAME ASC")
    public abstract List<NationalFederation> getAllSortedAlphabetically();
}
