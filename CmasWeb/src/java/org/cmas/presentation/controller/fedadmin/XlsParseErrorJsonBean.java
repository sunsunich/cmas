package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.annotations.Expose;

/**
 * Created on Jun 26, 2017
 *
 * @author Alexander Petukhov
 */
public class XlsParseErrorJsonBean {
    @Expose
    private boolean success;

    @Expose
    private String cause;

    @Expose
    private String rowNumber;

    public XlsParseErrorJsonBean() {
        success = false;
    }

    public XlsParseErrorJsonBean(String cause, String rowNumber) {
        this();
        this.cause = cause;
        this.rowNumber = rowNumber;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setIsSuccess(boolean success) {
        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }
}


