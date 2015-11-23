package org.cmas.entities.sport;

import org.cmas.entities.User;
import org.cmas.entities.UserBalance;

import javax.persistence.*;
import java.util.List;

/**
 * Created on Nov 15, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "sportsmen")
public class Sportsman extends User {

    @OneToOne
    private UserBalance userBalance;

    public Sportsman() {
    }

    public Sportsman(long id) {
        super(id);
    }

    @OneToOne
    private SportsmanCard primarySportsmanCard;

    @OneToMany(mappedBy = "sportsman")
    private List<SportsmanCard> secondarySportsmanCards;

    @ManyToOne
    private SportsFederation federation;

    public SportsmanCard getPrimarySportsmanCard() {
        return primarySportsmanCard;
    }

    public void setPrimarySportsmanCard(SportsmanCard primarySportsmanCard) {
        this.primarySportsmanCard = primarySportsmanCard;
    }

    public List<SportsmanCard> getSecondarySportsmanCards() {
        return secondarySportsmanCards;
    }

    public void setSecondarySportsmanCards(List<SportsmanCard> secondarySportsmanCards) {
        this.secondarySportsmanCards = secondarySportsmanCards;
    }

    public SportsFederation getFederation() {
        return federation;
    }

    public void setFederation(SportsFederation federation) {
        this.federation = federation;
    }

    @Override
    public UserBalance getUserBalance() {
        return userBalance;
    }

    @Override
    public void setUserBalance(UserBalance userBalance) {
        this.userBalance = userBalance;
    }
}
