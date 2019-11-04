package org.cmas.entities.cards;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.sport.Athlete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created on Nov 16, 2015
 *
 * @author Alexander Petukhov
 */
@Table
@Entity(name = "personal_cards")
public class PersonalCard  extends CardData implements Serializable {

    private static final long serialVersionUID = -656720490110835588L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Expose
    @Column(length = Globals.HALF_MAX_LENGTH)
    private String number;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private Diver diver;

    @Expose
    @Column
    private String imageUrl;

    @Override
    public String toString() {
        return "PersonalCard{" +
               "number='" + getNumber() + '\'' +
               ", diverType=" + getDiverType() +
               ", diverLevel=" + getDiverLevel() +
               ", cardType=" + getCardType() +
               ", federationName='" + getFederationName() + '\'' +
               '}';
    }

    @SuppressWarnings("CallToSimpleGetterFromWithinClass")
    public String getPrintName() {
        CardPrintInfo cardPrintInfo = CardPrintUtil.toPrintName(this);
        DiverLevel dl = getDiverLevel() == null ? DiverLevel.ONE_STAR : getDiverLevel();
        return cardPrintInfo.drawStars ? cardPrintInfo.printName + ' ' + dl.name() : cardPrintInfo.printName;
    }

    public String getPrintNumber() {
        if (getCardType() == PersonalCardType.PRIMARY) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - getNumber().length(); i++) {
                result.append('0');
                if ((i + 1) % 4 == 0) {
                    result.append(' ');
                }
            }
            for (int i = Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - getNumber().length();
                 i < Globals.SPORTS_CARD_NUMBER_MAX_LENGTH;
                 i++) {
                result.append(getNumber().charAt(i + getNumber().length() - Globals.SPORTS_CARD_NUMBER_MAX_LENGTH));
                if ((i + 1) % 4 == 0 && i != Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - 1) {
                    result.append(' ');
                }
            }
            return result.toString();
        }
        return getNumber();
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
