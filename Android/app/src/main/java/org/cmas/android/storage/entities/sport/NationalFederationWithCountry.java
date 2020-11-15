package org.cmas.android.storage.entities.sport;

import androidx.room.Embedded;
import androidx.room.Relation;
import org.cmas.android.storage.entities.Country;
import com.google.myjson.annotations.Expose;

public class NationalFederationWithCountry {

    @Embedded
    public NationalFederation nationalFederation;

    @Relation(
            parentColumn = "ID",
            entityColumn = "COUNTRY_ID"
    )
    @Expose
    public Country country;
}
