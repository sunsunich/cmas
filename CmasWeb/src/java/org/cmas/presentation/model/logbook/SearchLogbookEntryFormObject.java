package org.cmas.presentation.model.logbook;

import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

/**
 * Created on Jul 09, 2016
 *
 * @author Alexander Petukhov
 */
public class SearchLogbookEntryFormObject implements Validatable {

    private String toDateTimestamp;

    private String fromDateTimestamp;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String country;

    private String fromMeters;
    private String toMeters;

    private String fromMinutes;
    private String toMinutes;

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
        if (!StringUtil.isTrimmedEmpty(fromMeters)) {
            ValidatorUtils.validateInteger(
                    errors, fromMeters, "fromMeters", "validation.incorrectNumber"
            );
        }
        if (!StringUtil.isTrimmedEmpty(toMeters)) {
            ValidatorUtils.validateInteger(
                    errors, toMeters, "toMeters", "validation.incorrectNumber"
            );
        }
        if (!StringUtil.isTrimmedEmpty(fromMinutes)) {
            ValidatorUtils.validateInteger(
                    errors, fromMinutes, "fromMinutes", "validation.incorrectNumber"
            );
        }
        if (!StringUtil.isTrimmedEmpty(toMinutes)) {
            ValidatorUtils.validateInteger(
                    errors, toMinutes, "toMinutes", "validation.incorrectNumber"
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFromMeters() {
        return fromMeters;
    }

    public void setFromMeters(String fromMeters) {
        this.fromMeters = fromMeters;
    }

    public String getToMeters() {
        return toMeters;
    }

    public void setToMeters(String toMeters) {
        this.toMeters = toMeters;
    }

    public String getFromMinutes() {
        return fromMinutes;
    }

    public void setFromMinutes(String fromMinutes) {
        this.fromMinutes = fromMinutes;
    }

    public String getToMinutes() {
        return toMinutes;
    }

    public void setToMinutes(String toMinutes) {
        this.toMinutes = toMinutes;
    }
}
