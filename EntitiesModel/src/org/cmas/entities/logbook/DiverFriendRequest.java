package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "diver_friend_requests")
public class DiverFriendRequest {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Expose
    @ManyToOne
    private Diver from;

    @Expose
    @ManyToOne
    private Diver to;

    protected DiverFriendRequest() {
    }

    public DiverFriendRequest(Diver from, Diver to) {
        this.from = from;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Diver getFrom() {
        return from;
    }

    public void setFrom(Diver from) {
        this.from = from;
    }

    public Diver getTo() {
        return to;
    }

    public void setTo(Diver to) {
        this.to = to;
    }
}
