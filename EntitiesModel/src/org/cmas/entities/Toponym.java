package org.cmas.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Dec 03, 2016
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "toponyms")
public class Toponym extends DictionaryEntity{
    private static final long serialVersionUID = -548009636863242191L;

    @ManyToMany
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
