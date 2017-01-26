package org.cmas.presentation.model.logbook;

import com.google.myjson.annotations.Expose;
import org.cmas.entities.logbook.LogbookEntry;

/**
 * Created on Aug 13, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookEntryRequestFormObject extends LogbookEntry {

    private static final long serialVersionUID = 4556527963640783825L;

    @Expose
    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
