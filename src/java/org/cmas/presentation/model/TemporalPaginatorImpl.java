package org.cmas.presentation.model;

import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.Globals;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;

public abstract class TemporalPaginatorImpl<E extends Enum<E>> extends SortPaginatorImpl<E> implements Validatable {

    protected String fromDate;
    protected String toDate;

    protected TemporalPaginatorImpl(@NotNull E sortColumn) {
        super(sortColumn);
    }

    protected TemporalPaginatorImpl(int limit, int offset, @NotNull E sortColumn) {
        super(limit, offset, sortColumn);
    }

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDate(errors,fromDate,"fromDate","validation.incorrectDate", Globals.getDTF());
        ValidatorUtils.validateDate(errors,toDate,"toDate","validation.incorrectDate", Globals.getDTF());
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
