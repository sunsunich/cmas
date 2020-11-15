package org.cmas.entities.sport;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.diver.Diver;

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
@Table(name = "national_federations")
public class NationalFederation extends DictionaryEntity {

    private static final long serialVersionUID = -1462190485394499814L;
    @Expose
    @ManyToOne
    private Country country;

    @Expose
    private String email;

    @OneToMany(mappedBy = "federation")
    private List<Athlete> athleteList;

    @OneToMany(mappedBy = "federation")
    private List<Diver> diverList;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Athlete> getAthleteList() {
        return athleteList;
    }

    public void setAthleteList(List<Athlete> athleteList) {
        this.athleteList = athleteList;
    }

    public List<Diver> getDiverList() {
        return diverList;
    }

    public void setDiverList(List<Diver> diverList) {
        this.diverList = diverList;
    }
}
