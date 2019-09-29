package org.cmas.presentation.validator.user;

import org.cmas.Globals;
import org.cmas.entities.logbook.DiveSpec;
import org.cmas.entities.logbook.ScubaTank;
import org.cmas.presentation.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class DiveSpecValidator {

    @Autowired
    private ScubaTankValidator scubaTankValidator;

    public void validate(DiveSpec diveSpec, Errors errors, boolean isForCertification) {
        if (diveSpec.getMaxDepthMeters() <= 0) {
            errors.rejectValue("maxDepthMeters", "validation.incorrectNumber");
        }
        if (diveSpec.getDurationMinutes() <= 0) {
            errors.rejectValue("durationMinutes", "validation.incorrectNumber");
        }
        ValidatorUtils.validateLength(
                errors, diveSpec.getDecoStepsComments(), "decoStepsComments",
                "validation.maxLength", Globals.VERY_BIG_MAX_LENGTH
        );
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
                if (isForCertification) {
                    errors.rejectValue("scubaTanks", "validation.logbook.gasTanksEmpty");
                }
            }
        }
    }
}
