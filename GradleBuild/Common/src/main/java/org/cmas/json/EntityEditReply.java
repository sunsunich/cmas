package org.cmas.json;

import com.google.myjson.annotations.Expose;

public class EntityEditReply {

    @Expose
    protected long version;

    public EntityEditReply() {
    }

    public EntityEditReply(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
