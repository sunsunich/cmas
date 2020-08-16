package org.cmas.presentation.model.cards;

import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

public class CardApprovalRequestEditFormObject extends CommonCardApprovalRequestFormObject {

    @NotEmpty
    private String requestId;

    @Override
    public void validate(Errors errors) {
        super.validate(errors);
        ValidatorUtils.validateLong(errors, requestId, "requestId", "validation.incorrectField");
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
