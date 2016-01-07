package org.cmas.json.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.EntityEditReply;

import java.util.Date;

public class LogbookEntryCreateReply extends EntityEditReply {

    @Expose
    private long id;

    @Expose
    private Date dateCreation;

    public LogbookEntryCreateReply() {
    }

    public LogbookEntryCreateReply(LogbookEntry logbookEntry) {
        super(logbookEntry.getVersion());
        id = logbookEntry.getId();
        dateCreation = logbookEntry.getDateCreation();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}
