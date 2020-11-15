package org.cmas.android.storage.entities.cards;

import androidx.room.ColumnInfo;
import com.google.myjson.annotations.Expose;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;

import java.util.Date;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public class CardData {

    @Expose
    @ColumnInfo(name = "DIVER_TYPE")
    public DiverType diverType;

    @Expose
    @ColumnInfo(name = "DIVER_LEVEL")
    public DiverLevel diverLevel;

    @Expose
    @ColumnInfo(name = "PERSONAL_CARD_TYPE")
    public PersonalCardType cardType;

    @Expose
    @ColumnInfo(name = "FEDERATION_NAME")
    public String federationName;

    @Expose
    @ColumnInfo(name = "VALID_UNTIL")
    public Date validUntil;
}
