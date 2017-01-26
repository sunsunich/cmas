package org.cmas.remote.json;

import com.google.myjson.annotations.Expose;

/**
 * Created on Dec 04, 2016
 *
 * @author Alexander Petukhov
 */
public class SuccessIdObject extends IdObject{

    @Expose
    private boolean success;

    public SuccessIdObject(Long id) {
        super(id);
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
