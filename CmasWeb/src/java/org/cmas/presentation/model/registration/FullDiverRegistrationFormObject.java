package org.cmas.presentation.model.registration;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Jun 18, 2018
 *
 * @author Alexander Petukhov
 */
public class FullDiverRegistrationFormObject extends BasicDiverRegistrationFormObject {

    @Expose
    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String email;

    @Expose
    //todo implement sms
    private String phone;

    @Expose
    private String federationId;

    @Expose
    private String areaOfInterest;

    @Expose
    private List<String> images = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
