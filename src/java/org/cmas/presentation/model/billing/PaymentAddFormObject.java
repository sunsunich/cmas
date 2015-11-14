package org.cmas.presentation.model.billing;

import org.cmas.presentation.validator.Validatable;
import org.hibernate.validator.Pattern;
import org.springframework.validation.Errors;

import java.math.BigDecimal;


public class PaymentAddFormObject implements Validatable {

    @Pattern(regex = "[1-9][0-9]*(\\.[0-9]{1,2})?", message = "validation.incorrectNumber")
    private String amount;

    private String paymentType = "INTERKASSA";

//	private String currencyType;
//
//	public String getCurrencyType() {
//		return currencyType;
//	}

//	public void setCurrencyType(String currencyType) {
//		this.currencyType = currencyType;
//	}

    @Override
    public void validate(Errors errors) {
        if (!errors.hasFieldErrors("amount")) {
            try {

                BigDecimal bdAmount = new BigDecimal(amount);
                if (bdAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.rejectValue("amount", "validation.incorrectNumber");
                }
            }
            catch (Exception e) {
                errors.rejectValue("amount", "validation.incorrectNumber");
            }
        }
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
