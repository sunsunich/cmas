package org.cmas.android.storage.entities.cards;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.cards.PersonalCardType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created on Nov 16, 2015
 *
 * @author Alexander Petukhov
 */
@Entity(tableName = "PERSONAL_CARD")
public class PersonalCard extends CardData implements Serializable {

    private static final long serialVersionUID = -656720490110835588L;

    @Expose
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public long id;

    @Expose
    @ColumnInfo(name = "NUMBER")
    public String number;

    @Expose
    @ColumnInfo(name = "IMAGE_URL")
    public String imageUrl;

    @ColumnInfo(name = "DIVER_ID")
    public long diverId;

    public String getPrintNumber() {
        if (cardType == PersonalCardType.PRIMARY) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - number.length(); i++) {
                result.append('0');
                if ((i + 1) % 4 == 0) {
                    result.append(' ');
                }
            }
            for (int i = Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - number.length();
                 i < Globals.SPORTS_CARD_NUMBER_MAX_LENGTH;
                 i++) {
                result.append(number.charAt(i + number.length() - Globals.SPORTS_CARD_NUMBER_MAX_LENGTH));
                if ((i + 1) % 4 == 0 && i != Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - 1) {
                    result.append(' ');
                }
            }
            return result.toString();
        }
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalCard)) {
            return false;
        }
        PersonalCard that = (PersonalCard) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
    
