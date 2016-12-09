package org.cmas.service;

public class ReLoginException extends Exception {

    public ReLoginException() {
    }

    public ReLoginException(String detailMessage) {
        super(detailMessage);
    }

    public ReLoginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ReLoginException(Throwable throwable) {
        super(throwable);
    }
}
