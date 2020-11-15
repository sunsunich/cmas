package org.cmas.android.storage.entities.sport;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import org.cmas.android.storage.entities.DictionaryEntity;
import com.google.myjson.annotations.Expose;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity(tableName = "NATIONAL_FEDERATION")
public class NationalFederation extends DictionaryEntity {

    private static final long serialVersionUID = -1462190485394499814L;

    @Expose
    @ColumnInfo(name = "EMAIL")
    public String email;

    @ColumnInfo(name = "COUNTRY_ID")
    public long countryId;
}
