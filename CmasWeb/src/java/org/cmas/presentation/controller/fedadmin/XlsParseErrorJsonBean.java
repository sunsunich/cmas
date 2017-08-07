package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.annotations.Expose;

/**
 * Created on Jun 26, 2017
 *
 * @author Alexander Petukhov
 */
public class XlsParseErrorJsonBean {

    @Expose
    private String cause;

    @Expose
    private String rowNumber;

    public XlsParseErrorJsonBean() {
    }

    public XlsParseErrorJsonBean(String cause, String rowNumber) {
        this.cause = cause;
        this.rowNumber = rowNumber;
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


