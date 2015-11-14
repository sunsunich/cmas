package org.cmas.presentation.controller;


public class PageLoadErrorException extends RuntimeException {

    private static final String FORWARD_TO = "forward:/secure/pageErrorLoad.html?errorCode=";

    private String respMessage;
    private Integer errorCode;

    private StringBuilder builder = new StringBuilder(FORWARD_TO);

    public PageLoadErrorException(String respMessage, Integer errorCode) {
        super();
        this.respMessage = respMessage;
        this.errorCode = errorCode;
    }

    public PageLoadErrorException(String respMessage, Exception error) {
        super();
        this.respMessage = respMessage + " (" + error.getMessage() + ")";
        errorCode = 1111;
    }


    public String getForward() {
        builder.append(errorCode);
        if (respMessage != null) {
            builder.append("&respMessage=");
            builder.append(respMessage);
        }
        return builder.toString();
    }

}
