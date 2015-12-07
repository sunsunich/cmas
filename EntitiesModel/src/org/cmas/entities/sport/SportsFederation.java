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
    private List<Athlete> athleteList;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Athlete> getAthleteList() {
        return athleteList;
    }

    public void setAthleteList(List<Athlete> athleteList) {
        this.athleteList = athleteList;
    }
}
