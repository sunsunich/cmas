package org.cmas.presentation.model.logbook;

import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.springframework.validation.Errors;

/**
 * Created on Jul 09, 2016
 *
 * @author Alexander Petukhov
 */
public class SearchLogbookEntryFormObject implements Validatable {

    private String toDateTimestamp;

    private String fromDateTimestamp;

    private String limit;

    @Override
    public void validate(Errors errors) {
        if (!StringUtil.isTrimmedEmpty(fromDateTimestamp)) {
            ValidatorUtils.validateLong(
                    errors, fromDateTimestamp, "fromDateTimestamp", "validation.incorrectField"
            );
        }
        if (!StringUtil.isTrimmedEmpty(toDateTimestamp)) {
            ValidatorUtils.validateLong(
                    errors, toDateTimestamp, "toDateTimestamp", "validation.incorrectField"
            );
        }
        if (!StringUtil.isTrimmedEmpty(limit)) {
            ValidatorUtils.validateInteger(
                    errors, limit, "limit", "validation.incorrectNumber"
            );
        }
    }

    public String getToDateTimestamp() {
        return toDateTimestamp;
    }

    public void setToDateTimestamp(String toDateTimestamp) {
        this.toDateTimestamp = toDateTimestamp;
    }

    public String getFromDateTimestamp() {
        return fromDateTimestamp;
    }

    public void setFromDateTimestamp(String fromDateTimestamp) {
        this.fromDateTimestamp = fromDateTimestamp;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
