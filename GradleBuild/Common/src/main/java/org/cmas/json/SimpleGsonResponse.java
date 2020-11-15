package org.cmas.json;

import com.google.myjson.annotations.Expose;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 07/12/12
 * Time: 17:06
 */
public class SimpleGsonResponse {

    @Expose
    private Boolean success;

    @Expose
    private String message;

    public SimpleGsonResponse() {
    }

    public SimpleGsonResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
