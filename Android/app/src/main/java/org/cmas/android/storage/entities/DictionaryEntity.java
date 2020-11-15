package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import com.google.myjson.annotations.Expose;


public class DictionaryEntity extends IdEntity {

    private static final long serialVersionUID = -8308397577540494864L;

    @Expose
    @ColumnInfo(name = "NAME")
    public String name;

    @Expose
    @ColumnInfo(name = "VERSION")
    public long version;

    @Expose
    @ColumnInfo(name = "DELETED")
    public boolean deleted;
}
