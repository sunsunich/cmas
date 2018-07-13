package org.cmas.presentation.model.registration;

import org.cmas.util.StringUtil;
import org.springframework.validation.Errors;


public class DiverRegistrationFormObject extends BasicDiverRegistrationFormObject {

    private String certificate;

    @Override
    public void validate(Errors errors) {
        if (StringUtil.isTrimmedEmpty(certificate)) {
            super.validate(errors);
        } else {
            validateTermsAndCond(errors);
        }
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
