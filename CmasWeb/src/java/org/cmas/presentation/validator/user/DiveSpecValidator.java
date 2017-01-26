package org.cmas.presentation.validator.user;

import org.cmas.entities.logbook.DiveSpec;
import org.cmas.entities.logbook.ScubaTank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class DiveSpecValidator implements Validator {

    @Autowired
    private ScubaTankValidator scubaTankValidator;

    @Override
    public boolean supports(Class aClass) {
        return DiveSpec.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DiveSpec diveSpec = (DiveSpec) o;
        if (diveSpec.getMaxDepthMeters() <= 0) {
            errors.rejectValue("maxDepthMeters", "validation.incorrectNumber");
        }
        if (diveSpec.getDurationMinutes() <= 0) {
            errors.rejectValue("durationMinutes", "validation.incorrectNumber");
        }
        List<ScubaTank> scubaTanks = diveSpec.getScubaTanks();
        boolean hasScubaTanks = scubaTanks != null && !scubaTanks.isEmpty();
        if (diveSpec.getIsApnea()) {
            if (hasScubaTanks) {
                errors.rejectValue("scubaTanks", "validation.logbook.gasTanksAndApnea");
            }
        } else {
            if (hasScubaTanks) {
                for (int i = 0; i < scubaTanks.size(); i++) {
                    int index = i + 1;
                    ScubaTank scubaTank = scubaTanks.get(i);
                    scubaTankValidator.validate(scubaTank, errors, "_" + index);
                }
            } else {
                errors.rejectValue("scubaTanks", "validation.logbook.gasTanksEmpty");
            }
        }
    }
}
