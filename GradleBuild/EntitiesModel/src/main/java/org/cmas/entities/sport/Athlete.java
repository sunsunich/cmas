package org.cmas.entities.sport;

import org.cmas.entities.cards.CardUser;
import org.cmas.entities.cards.PersonalCard;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Nov 15, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "athletes")
public class Athlete extends CardUser {


    @OneToMany(mappedBy = "athlete")
    private List<PersonalCard> cards;

    public Athlete() {
    }

    public Athlete(long id) {
        super(id);
    }

    public List<PersonalCard> getCards() {
        return cards;
    }

    public void setCards(List<PersonalCard> cards) {
        this.cards = cards;
    }
}
