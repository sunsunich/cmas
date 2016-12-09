package org.cmas.util.http;

public class HttpException extends Exception{
    private static final long serialVersionUID = -857764665252711759L;

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }
}
