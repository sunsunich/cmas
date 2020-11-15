package org.cmas.remote.json;

import com.google.myjson.annotations.Expose;

/**
 * Created on Dec 04, 2016
 *
 * @author Alexander Petukhov
 */
public class IdObject {

    @Expose
    private Long id;

    public IdObject(Long id) {
        this.id = id;
    }

    public IdObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
