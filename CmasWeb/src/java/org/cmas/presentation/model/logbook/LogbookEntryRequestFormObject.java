package org.cmas.presentation.model.logbook;

/**
 * Created on Aug 13, 2016
 *
 * @author Alexander Petukhov
 */
public class LogbookEntryRequestFormObject extends LogbookEntryFormObject {

    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
