package org.cmas.json.user;

import com.google.myjson.annotations.Expose;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 12/12/12
 * Time: 00:26
 */
public class RegisterNewUserReply {

    @Expose
    private Long id;

    public RegisterNewUserReply(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
