package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import com.google.myjson.annotations.Expose;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity(tableName = "COUNTRY")
public class Country extends DictionaryEntity {

    private static final long serialVersionUID = 3857192785221652490L;

    @Expose
    @ColumnInfo(name = "CODE")
    public String code;

    @Expose
    @ColumnInfo(name = "ISO3166_1_ALPHA_2_CODE")
    public String iso3166_1_alpha_2_code;
}
