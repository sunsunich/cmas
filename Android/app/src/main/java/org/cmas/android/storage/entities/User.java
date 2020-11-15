package org.cmas.android.storage.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import com.google.myjson.annotations.Expose;
import org.cmas.entities.HasId;

import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable, HasId {

    private static final long serialVersionUID = -5985814100072616561L;

    //set by server
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public long id;

    //end set by server

    //set by user from mobile
    @Expose
    @ColumnInfo(name = "FIRST_NAME")
    public String firstName;

    @Expose
    @ColumnInfo(name = "LAST_NAME")
    public String lastName;

    @ColumnInfo(name = "COUNTRY_ID")
    public long countryId;

    @Expose
    @ColumnInfo(name = "USERPIC_URL")
    public String userpicUrl;

    //end set by user from mobile

    @Expose
    @ColumnInfo(name = "MOBILE_AUTH_TOKEN")
    public String mobileAuthToken;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public long getId() {
        return id;
    }
}
