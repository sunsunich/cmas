package org.cmas.entities;

import com.google.myjson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created on May 27, 2019
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "addresses")
public class Address extends DictionaryEntity{

    private static final long serialVersionUID = 685310177284769825L;

    @Expose
    @ManyToOne
    private Country country;

    // optional
    @Expose
    private String region;

    @Expose
    @Column(nullable = false)
    private String zipCode;

    @Expose
    @Column(nullable = false)
    private String city;

    @Expose
    @Column(nullable = false)
    private String street;

    @Expose
    @Column(nullable = false)
    private String house;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
}
