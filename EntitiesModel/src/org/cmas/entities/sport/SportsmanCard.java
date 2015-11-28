package org.cmas.entities.sport;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;

import javax.persistence.*;

/**
 * Created on Nov 16, 2015
 *
 * @author Alexander Petukhov
 */
@Table
@Entity(name = "sportsman_cards")
public class SportsmanCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private long id;

    @Column(length = Globals.HALF_MAX_LENGTH)
    private String number;

    @ManyToOne
    private Sportsman sportsman;

    @ManyToOne
    private SportsmanCardType sportsmanCardType;

    public SportsmanCardType getSportsmanCardType() {
        return sportsmanCardType;
    }

    public void setSportsmanCardType(SportsmanCardType sportsmanCardType) {
        this.sportsmanCardType = sportsmanCardType;
    }

    public Sportsman getSportsman() {
        return sportsman;
    }

    public void setSportsman(Sportsman sportsman) {
        this.sportsman = sportsman;
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
}
