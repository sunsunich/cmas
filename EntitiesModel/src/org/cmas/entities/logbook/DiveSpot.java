package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

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
    @Column(nullable = false)
    private Date dateCreation;

    @Expose
    @Column(nullable = true)
    private Date dateEdit;

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
