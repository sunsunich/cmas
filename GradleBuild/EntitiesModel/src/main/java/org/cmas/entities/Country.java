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

    public static final String IRAN_COUNTRY_CODE = "IRI";
    public static final String RUSSIAN_COUNTRY_CODE = "RUS";
    public static final String EGYPT_COUNTRY_CODE = "EGY";

    private static final long serialVersionUID = 3857192785221652490L;

    public Country() {
    }

    public Country(String code) {
        this();
        this.code = code;
    }

    @Expose
    private String code;

    @Expose
    private String iso3166_1_alpha_2_code;

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

    public String getIso3166_1_alpha_2_code() {
        return iso3166_1_alpha_2_code;
    }

    public void setIso3166_1_alpha_2_code(String iso3166_1_alpha_2_code) {
        this.iso3166_1_alpha_2_code = iso3166_1_alpha_2_code;
    }
}
