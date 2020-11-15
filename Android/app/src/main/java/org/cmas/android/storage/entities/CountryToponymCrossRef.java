package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "COUNTRy_TOPONYM", primaryKeys = {"COUNTRY_ID", "TOPONYM_ID"})
public class CountryToponymCrossRef {

    @ColumnInfo(name = "COUNTRY_ID")
    public long countryId;

    @ColumnInfo(name = "TOPONYM_ID")
    public long toponymId;
}
