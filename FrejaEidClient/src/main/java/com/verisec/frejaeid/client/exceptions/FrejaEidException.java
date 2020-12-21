package com.verisec.frejaeid.client.exceptions;

/**
 * Occurs when server returns an error.
 */
public class FrejaEidException extends Exception {

    private int errorCode;

    public FrejaEidException(String message) {
        super(message);
    }

    public FrejaEidException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
