package org.cmas.entities.divespot;

import org.cmas.entities.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created on Dec 27, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "dive_spots")
public class DiveSpot extends DictionaryEntity {

    private static final long serialVersionUID = 563219826507639916L;

    @Column
    private double longitude;

    @Column
    private double latitude;

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
}
