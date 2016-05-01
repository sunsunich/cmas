package org.cmas.entities;

import com.google.myjson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "countries")
public class Country extends DictionaryEntity {

    private static final long serialVersionUID = 3857192785221652490L;

    public static final Country EMPTY_COUNTRY = new Country();

    public Country() {
    }

    public Country(String code) {
        this.code = code;
    }

    @Expose
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
