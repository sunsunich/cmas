package org.cmas.entities.diver;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.logbook.LogbookEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Dec 04, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "divers")
public class Diver extends CardUser {

    private static final long serialVersionUID = -6873304958863096818L;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverLevel diverLevel;

    @Expose
    @Column
    @Enumerated(EnumType.STRING)
    private DiverType diverType;

    @OneToMany(mappedBy = "diver")
    private List<LogbookEntry> logbookEntries;

    @OneToMany(mappedBy = "diver")
    private List<PersonalCard> cards;

    @ManyToOne
    private Diver instructor;

    @Expose
    @Column
    private boolean hasPayed;

    public Diver() {
    }

    public Diver(long id) {
        super(id);
    }

    public List<LogbookEntry> getLogbookEntries() {
        return logbookEntries;
    }

    public void setLogbookEntries(List<LogbookEntry> logbookEntries) {
        this.logbookEntries = logbookEntries;
    }

    public DiverLevel getDiverLevel() {
        return diverLevel;
    }

    public void setDiverLevel(DiverLevel diverLevel) {
        this.diverLevel = diverLevel;
    }

    public DiverType getDiverType() {
        return diverType;
    }

    public void setDiverType(DiverType diverType) {
        this.diverType = diverType;
    }

    public boolean isHasPayed() {
        return hasPayed;
    }

    public void setHasPayed(boolean hasPayed) {
        this.hasPayed = hasPayed;
    }

    public List<PersonalCard> getCards() {
        return cards;
    }

    public void setCards(List<PersonalCard> cards) {
        this.cards = cards;
    }

    public Diver getInstructor() {
        return instructor;
    }

    public void setInstructor(Diver instructor) {
        this.instructor = instructor;
    }

    @SuppressWarnings("MagicCharacter")
    @Override
    public String toString() {
        StringBuilder cardStr = new StringBuilder();
        cardStr.append('[');
        if (cards != null) {
            for (PersonalCard card : cards) {
                cardStr.append(card).append(';');
            }
        }
        cardStr.append(']');
        return "Diver{" +
               "instructor=" + instructor +
               ", diverLevel=" + diverLevel +
               ", diverType=" + diverType +
               ", cards=" + cardStr +
               "} " + super.toString();
    }
}
