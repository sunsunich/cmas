package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.springframework.validation.Errors;

public class DiverVerificationAjaxFormObject extends  DiverVerificationFormObject {

    private String universalCmasId;

    @Override
    public void validate(Errors errors) {
        if (StringUtil.isTrimmedEmpty(universalCmasId)) {
            super.validate(errors);
        } else {
            ValidatorUtils.validateLength(errors,
                                          universalCmasId,
                                          "universalCmasId",
                                          "validation.maxLength",
                                          Globals.MAX_LENGTH);
        }
    }

    public String getUniversalCmasId() {
        return universalCmasId;
    }

    public void setUniversalCmasId(String universalCmasId) {
        this.universalCmasId = universalCmasId;
    }
}
