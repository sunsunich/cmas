package org.cmas.android.storage.entities.diver;

import androidx.room.Embedded;
import androidx.room.Relation;
import org.cmas.android.storage.entities.Country;
import org.cmas.android.storage.entities.cards.PersonalCard;
import org.cmas.android.storage.entities.sport.NationalFederationWithCountry;
import com.google.myjson.annotations.Expose;

import java.util.List;

public class DiverWithAllData {

    @Embedded
    public Diver diver;

    @Expose
    @Relation(
            parentColumn = "ID",
            entityColumn = "INSTRUCTOR_ID"
    )
    public Diver instructor;

    @Expose
    @Relation(
            parentColumn = "ID",
            entityColumn = "DIVER_ID"
    )
    public PersonalCard primaryPersonalCard;

    @Expose
    @Relation(
            parentColumn = "NATIONAL_FEDERATION_ID",
            entityColumn = "ID"
    )
    public NationalFederationWithCountry federation;

    @Expose
    @Relation(
            parentColumn = "COUNTRY_ID",
            entityColumn = "ID"
    )
    public Country country;

    @Expose
    @Relation(
            parentColumn = "ID",
            entityColumn = "DIVER_ID"
    )
    public List<PersonalCard> cards;
}
