package org.cmas.backend.xls;

/**
 * Created on Jun 26, 2017
 *
 * @author Alexander Petukhov
 */
public class XlsParseException extends Exception {
    private static final long serialVersionUID = -2102708198620177210L;
    private final String rowNumber;

    public XlsParseException(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public XlsParseException(String rowNumber, String message) {
        super(message);
        this.rowNumber = rowNumber;
    }

    public XlsParseException(String rowNumber, String message, Throwable cause) {
        super(message, cause);
        this.rowNumber = rowNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }
}
