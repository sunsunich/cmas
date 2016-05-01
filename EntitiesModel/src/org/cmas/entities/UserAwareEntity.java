package org.cmas.entities;

import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created on Nov 24, 2015
 *
 * @author Alexander Petukhov
 */
@MappedSuperclass
public class UserAwareEntity {

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Athlete.class)
    protected Athlete athlete;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Amateur.class)
    protected Amateur amateur;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Diver.class)
    protected Diver diver;

    @SuppressWarnings({"unchecked", "CallToSimpleGetterFromWithinClass"})
    public <T extends User> T getUser() {
        if (getAmateur() != null) {
            return (T) getAmateur();
        }
        if (getAthlete() != null) {
            return (T) getAthlete();
        }

        return (T) getDiver();

    }

    @SuppressWarnings("CallToSimpleSetterFromWithinClass")
    public final <T extends User> void setUser(T user) {
        switch (user.getRole()) {
            case ROLE_FEDERATION_ADMIN:
            case ROLE_ADMIN:
            case ROLE_DIVER:
                setDiver((Diver)user);
                break;
            case ROLE_AMATEUR:
                setAmateur((Amateur) user);
                break;
            case ROLE_ATHLETE:
                setAthlete((Athlete) user);
                break;
        }
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
    }

    public Diver getDiver() {
        return diver;
    }

    public void setDiver(Diver diver) {
        this.diver = diver;
    }
}
