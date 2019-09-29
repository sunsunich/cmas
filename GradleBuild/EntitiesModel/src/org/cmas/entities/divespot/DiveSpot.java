package org.cmas.entities.divespot;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.Toponym;
import org.cmas.entities.diver.Diver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created on Dec 27, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "dive_spots")
public class DiveSpot extends DictionaryEntity {

    private static final long serialVersionUID = 563219826507639916L;

    @Expose
    @Column
    private double latitude;

    @Expose
    @Column
    private double longitude;

    @Expose
    @ManyToOne
    private Country country;

    @Expose
    @ManyToOne
    private Toponym toponym;

    @Expose
    @Column
    private boolean isApproved;

    @Expose
    @Column
    private boolean isAutoGeoLocation;

    @Expose
    private String latinName;

    @ManyToOne
    private Diver creator;

    @Expose
    @Transient
    private boolean editable;

    public Diver getCreator() {
        return creator;
    }

    public void setCreator(Diver creator) {
        this.creator = creator;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public boolean isAutoGeoLocation() {
        return isAutoGeoLocation;
    }

    public void setIsAutoGeoLocation(boolean isAutoGeoLocation) {
        this.isAutoGeoLocation = isAutoGeoLocation;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Toponym getToponym() {
        return toponym;
    }

    public void setToponym(Toponym toponym) {
        this.toponym = toponym;
    }
}
