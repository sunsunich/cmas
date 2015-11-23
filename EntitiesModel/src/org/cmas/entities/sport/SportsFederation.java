package org.cmas.entities.sport;

import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "sports_federations")
public class SportsFederation extends DictionaryEntity {

    @ManyToOne
    private Country country;

    @OneToMany(mappedBy = "federation")
    private List<Sportsman> sportsmanList;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Sportsman> getSportsmanList() {
        return sportsmanList;
    }

    public void setSportsmanList(List<Sportsman> sportsmanList) {
        this.sportsmanList = sportsmanList;
    }
}
