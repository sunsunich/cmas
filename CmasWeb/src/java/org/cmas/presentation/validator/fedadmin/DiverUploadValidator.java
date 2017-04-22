package org.cmas.presentation.validator.fedadmin;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.validator.admin.EditUserValidator;
import org.cmas.util.StringUtil;
import org.springframework.validation.Errors;

import java.util.List;


public class DiverUploadValidator extends EditUserValidator {

    @Override
    public boolean supports(Class clazz) {
        return Diver.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);
        Diver diver = (Diver) target;
        if (StringUtil.isTrimmedEmpty(diver.getFirstName())) {
            errors.rejectValue("firstName", "validation.emptyField");
        }
        if (StringUtil.isTrimmedEmpty(diver.getLastName())) {
            errors.rejectValue("lastName", "validation.emptyField");
        }
        if (diver.getDob() == null) {
            errors.rejectValue("dob", "validation.emptyField");
        }
        if (diver.getDiverType() == null) {
            errors.rejectValue("diverType", "validation.emptyField");
        }
        if (diver.getDiverLevel() == null) {
            errors.rejectValue("diverLevel", "validation.emptyField");
        }
        if (diver.getCards() != null) {
            List<PersonalCard> cards = diver.getCards();
            for (int i = 0; i < cards.size(); i++) {
                PersonalCard card = cards.get(i);
                if (card.getCardType() == null) {
                    errors.rejectValue("cards[" + i + "].cardType", "validation.emptyField");
                }
                if (card.getDiverType() == null) {
                    errors.rejectValue("cards[" + i + "].diverType", "validation.emptyField");
                }
            }
        }
    }
}
