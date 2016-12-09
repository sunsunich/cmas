package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created on Jul 15, 2016
 *
 * @author Alexander Petukhov
 */
@MappedSuperclass
public class Request {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Expose
    @ManyToOne
    protected Diver from;

    @Expose
    @ManyToOne
    protected Diver to;

    protected Request() {
    }

    public Request(Diver from, Diver to) {
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
