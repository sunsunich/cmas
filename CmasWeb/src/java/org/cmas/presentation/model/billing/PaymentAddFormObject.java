package org.cmas.presentation.model.billing;

import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Pattern;
import org.springframework.validation.Errors;


public class PaymentAddFormObject implements Validatable {

    @SuppressWarnings("HardcodedFileSeparator")
    @Pattern(regex = "\\[['\"\\d\\s,]+\\]", message = "validation.incorrectField")
    private String featuresIdsJson;

    private String paymentType = "SYSTEMPAY";

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors, paymentType, InvoiceType.class, "paymentType", "validation.incorrectField");
    }

    public String getFeaturesIdsJson() {
        return featuresIdsJson;
    }

    public void setFeaturesIdsJson(String featuresIdsJson) {
        this.featuresIdsJson = featuresIdsJson;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
