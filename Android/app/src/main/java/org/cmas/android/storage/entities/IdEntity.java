package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import com.google.myjson.annotations.Expose;
import org.cmas.entities.HasId;

import java.io.Serializable;
import java.util.Objects;

public class IdEntity implements Serializable, HasId {

    private static final long serialVersionUID = -8308397577540344564L;

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public long id;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DictionaryEntity)) {
            return false;
        }
        DictionaryEntity that = (DictionaryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
