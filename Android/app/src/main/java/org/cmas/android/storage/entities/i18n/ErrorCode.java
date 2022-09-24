package org.cmas.android.storage.entities.i18n;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.myjson.annotations.Expose;

@Entity(tableName = "ERROR_CODE")
public class ErrorCode {

    public ErrorCode() {
    }

    public ErrorCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @NonNull
    @PrimaryKey
    @Expose
    @ColumnInfo(name = "CODE")
    public String code;

    @Expose
    @ColumnInfo(name = "VALUE")
    public String value;
}
