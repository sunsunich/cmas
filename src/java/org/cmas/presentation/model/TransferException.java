package org.cmas.presentation.model;


public class TransferException extends RuntimeException{

    public TransferException() {
        super();
    }

    public TransferException(Throwable cause) {
        super(cause);
    }

    public TransferException(String message) {
        super(message);
    }

    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
