package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.google.myjson.annotations.Expose;
import org.cmas.entities.UserFileType;

import java.util.Date;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
@Entity(tableName = "USER_FILE")
public class UserFile extends DictionaryEntity {

    @Expose
    @ColumnInfo(name = "DATE_CREATION")
    public Date dateCreation;

    @Expose
    @ColumnInfo(name = "DATE_EDIT")
    public Date dateEdit;

    @Expose
    @ColumnInfo(name = "FILE_URL")
    public String fileUrl;

    @Expose
    @ColumnInfo(name = "MIME_TYPE")
    public String mimeType;

    @Expose
    @ColumnInfo(name = "USER_FILE_TYPE")
    public UserFileType userFileType;

    @ColumnInfo(name = "CREATOR_ID")
    public long creatorId;
}
