package org.cmas.entities;

import com.google.myjson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "countries")
public class Country extends DictionaryEntity {

    private static final long serialVersionUID = 3857192785221652490L;

    public Country() {
    }

    public Country(String code) {
        this();
        this.code = code;
    }

    @Expose
    private String code;

    @ManyToMany
    private List<Toponym> toponyms;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Toponym> getToponyms() {
        return toponyms;
    }

    public void setToponyms(List<Toponym> toponyms) {
        this.toponyms = toponyms;
    }
}
