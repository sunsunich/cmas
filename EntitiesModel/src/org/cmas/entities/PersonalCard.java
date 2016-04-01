package org.cmas.entities;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
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
public class PersonalCard implements Serializable{

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
    @ManyToOne
    private PersonalCardType personalCardType;

    public PersonalCardType getPersonalCardType() {
        return personalCardType;
    }

    public void setPersonalCardType(PersonalCardType personalCardType) {
        this.personalCardType = personalCardType;
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
}
