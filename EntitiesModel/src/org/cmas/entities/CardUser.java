package org.cmas.entities;

import org.cmas.entities.sport.SportsFederation;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * Created on Dec 04, 2015
 *
 * @author Alexander Petukhov
 */
@MappedSuperclass
public abstract class CardUser extends User {

    private static final long serialVersionUID = -7021775181436569160L;

    protected CardUser() {
    }

    protected CardUser(long id) {
        super(id);
    }


    @OneToOne
    private PersonalCard primaryPersonalCard;


    @ManyToOne
    private SportsFederation federation;

    public PersonalCard getPrimaryPersonalCard() {
        return primaryPersonalCard;
    }

    public void setPrimaryPersonalCard(PersonalCard primaryPersonalCard) {
        this.primaryPersonalCard = primaryPersonalCard;
    }

    public SportsFederation getFederation() {
        return federation;
    }

    public void setFederation(SportsFederation federation) {
        this.federation = federation;
    }


}
