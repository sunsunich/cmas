package org.cmas.presentation.validator.user;

import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.DiveSpec;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class LogbookEntryValidator implements Validator {

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private DiveSpotValidator diveSpotValidator;

    @Autowired
    private DiveSpecValidator diveSpecValidator;

    @Override
    public boolean supports(Class aClass) {
        return LogbookEntry.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        LogbookEntry logbookEntry = (LogbookEntry) o;
        if (logbookEntry.getDiveDate() == null) {
            errors.rejectValue("diveDate", "validation.emptyField");
        }
        if (logbookEntry.getLatitude() <= 0.0) {
            errors.rejectValue("latitude", "validation.incorrectNumber");
        }
        if (logbookEntry.getLongitude() <= 0.0) {
            errors.rejectValue("longitude", "validation.incorrectNumber");
        }
        if (logbookEntry.getVisibility() == null) {
            errors.rejectValue("visibility", "validation.emptyField");
        }
        if (logbookEntry.getState() == null) {
            errors.rejectValue("state", "validation.emptyField");
        }
        DiveSpec diveSpec = logbookEntry.getDiveSpec();
        if (diveSpec == null) {
            errors.rejectValue("diveSpec", "validation.emptyField");
        } else {
            diveSpecValidator.validate(diveSpec, errors);
        }
        DiveSpot diveSpot = logbookEntry.getDiveSpot();
        if (diveSpot != null) {
            diveSpotValidator.validate(diveSpot, errors);
        }
    }
}