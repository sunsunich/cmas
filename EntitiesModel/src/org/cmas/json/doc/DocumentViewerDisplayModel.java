package org.cmas.json.doc;

import com.google.myjson.annotations.Expose;

public class DocumentViewerDisplayModel {

    //profile id
    @Expose
    private long id;

    //user (owner of profile) type id
    @Expose
    private long typeId;

    //profile name
    @Expose
    private String name;

    public DocumentViewerDisplayModel() {
    }

    public DocumentViewerDisplayModel(long id, long typeId, String name) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
