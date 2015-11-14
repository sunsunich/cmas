package org.cmas.presentation.dao;

public class DAOException extends RuntimeException{
    
    public DAOException() {
        super();
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause); 
    }
}
