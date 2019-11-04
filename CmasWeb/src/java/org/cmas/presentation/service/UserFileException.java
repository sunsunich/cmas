package org.cmas.presentation.service;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public class UserFileException extends Exception {

    private final String errorCode;

    public UserFileException(String errorCode) {
        this.errorCode = errorCode;
    }

    public UserFileException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserFileException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
