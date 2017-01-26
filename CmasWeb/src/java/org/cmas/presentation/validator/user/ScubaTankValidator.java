package org.cmas.presentation.validator.user;

import org.cmas.entities.logbook.ScubaTank;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class ScubaTankValidator implements Validator {

    @Override
    public boolean supports(Class aClass) {
        return ScubaTank.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        validate(o, errors, "");
    }

    public void validate(Object o, Errors errors, String index) {
        ScubaTank scubaTank = (ScubaTank) o;
        if (scubaTank.getSize() <= 0.0) {
            errors.rejectValue("size" + index, "validation.incorrectNumber");
        }
        if (scubaTank.getVolumeMeasureUnit() == null) {
            errors.rejectValue("volumeMeasureUnit" + index, "validation.emptyField");
        }
        if (scubaTank.getStartPressure() <= 0.0) {
            errors.rejectValue("startPressure" + index, "validation.incorrectNumber");
        }
        if (scubaTank.getEndPressure() < 0.0) {
            errors.rejectValue("endPressure" + index, "validation.incorrectNumber");
        }
        if (scubaTank.getPressureMeasureUnit() == null) {
            errors.rejectValue("pressureMeasureUnit" + index, "validation.emptyField");
        }
        if (scubaTank.getSupplyType() == null) {
            errors.rejectValue("supplyType" + index, "validation.emptyField");
        }
        if (!scubaTank.getIsAir()) {
            if (scubaTank.getOxygenPercent() <= 0.0) {
                errors.rejectValue("oxygenPercent" + index, "validation.incorrectNumber");
            }
        }
    }
}
