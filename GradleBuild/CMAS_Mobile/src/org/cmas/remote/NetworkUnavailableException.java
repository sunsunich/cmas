package org.cmas.remote;

public class NetworkUnavailableException extends Exception {
    public NetworkUnavailableException() {
    }

    public NetworkUnavailableException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkUnavailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkUnavailableException(Throwable throwable) {
        super(throwable);
    }
}
