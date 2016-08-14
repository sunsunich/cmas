package org.cmas.entities.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.diver.Diver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
@Entity
@Table(name = "logbook_buddie_requests")
public class LogbookBuddieRequest extends Request {

    @Expose
    @Column
    private boolean toSign;

    @Expose
    @ManyToOne
    private LogbookEntry logbookEntry;

    public LogbookBuddieRequest() {
    }

    public LogbookBuddieRequest(Diver from, Diver to) {
        super(from, to);
    }

    public LogbookEntry getLogbookEntry() {
        return logbookEntry;
    }

    public void setLogbookEntry(LogbookEntry logbookEntry) {
        this.logbookEntry = logbookEntry;
    }

    public boolean isToSign() {
        return toSign;
    }

    public void setToSign(boolean toSign) {
        this.toSign = toSign;
    }
}
