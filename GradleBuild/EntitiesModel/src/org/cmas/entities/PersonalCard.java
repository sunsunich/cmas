package org.cmas.entities;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.Athlete;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class PersonalCard implements Serializable {

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

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(length = Globals.DB_PIC_MAX_BYTE_SIZE)
    private byte[] image;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverType diverType;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverLevel diverLevel;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private PersonalCardType cardType;

    @Expose
    @Column
    private String federationName;

    @Override
    public String toString() {
        return "PersonalCard{" +
               "number='" + number + '\'' +
               ", diverType=" + diverType +
               ", diverLevel=" + diverLevel +
               ", cardType=" + cardType +
               ", federationName='" + federationName + '\'' +
               '}';
    }

    @SuppressWarnings("CallToSimpleGetterFromWithinClass")
    public String getPrintName() {
        CardPrintInfo cardPrintInfo = CardPrintUtil.toPrintName(this);
        DiverLevel dl = getDiverLevel() == null ? DiverLevel.ONE_STAR : getDiverLevel();
        return cardPrintInfo.drawStars ? cardPrintInfo.printName + ' ' + dl.name() : cardPrintInfo.printName;
    }

    public String getPrintNumber() {
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

    public DiverType getDiverType() {
        return diverType;
    }

    public void setDiverType(DiverType diverType) {
        this.diverType = diverType;
    }

    public String getFederationName() {
        return federationName;
    }

    public void setFederationName(String federationName) {
        this.federationName = federationName;
    }

    public PersonalCardType getCardType() {
        return cardType;
    }

    public void setCardType(PersonalCardType cardType) {
        this.cardType = cardType;
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

    public DiverLevel getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(DiverLevel diverLevel) {
        this.diverLevel = diverLevel;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
