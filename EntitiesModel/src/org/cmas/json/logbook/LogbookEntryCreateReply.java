package org.cmas.json.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.doc.Document;
import org.cmas.json.EntityEditReply;

import java.util.Date;

public class LogbookEntryCreateReply extends EntityEditReply {

    @Expose
    private long id;

    @Expose
    private Date dateCreation;

    public LogbookEntryCreateReply() {
    }

    public LogbookEntryCreateReply(Document document) {
        super(document.getVersion());
        id = document.getId();
        dateCreation = document.getDateCreation();
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
