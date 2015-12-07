package org.cmas.entities.sport;

import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;

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
    private List<PersonalCard> secondaryPersonalCards;

    public Athlete() {
    }

    public Athlete(long id) {
        super(id);
    }

    public List<PersonalCard> getSecondaryPersonalCards() {
        return secondaryPersonalCards;
    }

    public void setSecondaryPersonalCards(List<PersonalCard> secondaryPersonalCards) {
        this.secondaryPersonalCards = secondaryPersonalCards;
    }
}
