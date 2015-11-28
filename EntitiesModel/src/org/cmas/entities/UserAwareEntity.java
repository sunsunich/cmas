package org.cmas.entities;

import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.sport.Sportsman;

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

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Sportsman.class)
    protected Sportsman sportsman;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, targetEntity = Amateur.class)
    protected Amateur amateur;

    @SuppressWarnings("unchecked")
    public <T extends User> T getUser(){
        if(getAmateur() == null){
            return (T)getSportsman();
        }
        else{
            return (T)getAmateur();
        }
    }

    public <T extends User> void setUser(T user) {
        switch (user.getRole()) {
            case ROLE_ADMIN:
            case ROLE_AMATEUR:
                setAmateur((Amateur) user);
                break;
            case ROLE_SPORTSMAN:
                setSportsman((Sportsman) user);
                break;
        }
    }

    public Sportsman getSportsman() {
        return sportsman;
    }

    public void setSportsman(Sportsman sportsman) {
        this.sportsman = sportsman;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
    }
}
