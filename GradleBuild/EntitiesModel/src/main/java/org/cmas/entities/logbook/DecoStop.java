package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "deco_stops")
public class DecoStop implements Serializable{

    private static final long serialVersionUID = 1971937541735441453L;

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DiveSpec diveSpec;

    @Expose
    @Column
    private int depthMeters;

    @Expose
    @Column
    private int durationMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiveSpec getDiveSpec() {
        return diveSpec;
    }

    public void setDiveSpec(DiveSpec diveSpec) {
        this.diveSpec = diveSpec;
    }

    public int getDepthMeters() {
        return depthMeters;
    }

    public void setDepthMeters(int depthMeters) {
        this.depthMeters = depthMeters;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}
