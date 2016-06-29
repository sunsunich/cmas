package org.cmas.presentation.controller.user.billing;

import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

public abstract class PaySystemValidator implements Validator {

    @Autowired
    protected InvoiceDao invoiceDao;

    @Autowired
    protected PaySystemSettings paySystemSettings;

    @Autowired
    protected AuthenticationService authenticationService;

    @Autowired
    private MessageSource messageSource;

    protected void checkEmptyStringField(String fieldName, String fieldValue, Errors errors) {
        if (fieldValue == null || fieldValue.isEmpty()) {
            errors.rejectValue(fieldName, "validation.billing.paysystem.emptyField");
        }
    }

    public boolean isExternalInvoiceNumberValid(String externalInvoiceNumber) {
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
        if (invoice == null) {
            return false;
        }
        InvoiceType invoiceType = invoice.getInvoiceType();
        if (  invoiceType != InvoiceType.SYSTEMPAY
           ) {
            return false;
        }
        Athlete invoiceUser = invoice.getAthlete();
        if (invoiceUser == null) {
            return false;
        }
        BackendUser currentUser = authenticationService.getCurrentUser();
        //noinspection SimplifiableIfStatement
        if (currentUser == null) {
            return false;
        }
        return currentUser.getNullableId().equals(invoiceUser.getId());
    }

    public String makeMessageFromErrors(BindingResult errors) {
        StringBuilder result = new StringBuilder();
        if (errors.hasErrors()) {
            for (Object error : errors.getAllErrors()) {
                String[] codes = null;
                Object[] arguments = null;
                if (error instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) error;
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        result.append(fieldError.getField());
                        result.append(": ");
                    }
                    codes = objectError.getCodes();
                    arguments = objectError.getArguments();
                }
                if (codes != null) {
                    for (String code : codes) {
                        String messageFromSource = messageSource.getMessage(code, arguments, "", null);
                        if (!messageFromSource.isEmpty()) {
                            result.append(messageFromSource);
                            break;
                        }
                    }
                }
                result.append("\n");
            }
        }
        return result.toString();
    }
}
