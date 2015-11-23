package org.cmas.entities.amateur;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.DictionaryEntity;

import javax.persistence.*;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "mass_event_requests")
public class MassEventRequest extends DictionaryEntity{

    @Expose
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MassEventRequestStatus massEventRequestStatus;

    @ManyToOne
    private MassEvent massEvent;

    @ManyToOne
    private Amateur amateur;

    public MassEventRequestStatus getMassEventRequestStatus() {
        return massEventRequestStatus;
    }

    public void setMassEventRequestStatus(MassEventRequestStatus massEventRequestStatus) {
        this.massEventRequestStatus = massEventRequestStatus;
    }

    public MassEvent getMassEvent() {
        return massEvent;
    }

    public void setMassEvent(MassEvent massEvent) {
        this.massEvent = massEvent;
    }

    public Amateur getAmateur() {
        return amateur;
    }

    public void setAmateur(Amateur amateur) {
        this.amateur = amateur;
    }
}
