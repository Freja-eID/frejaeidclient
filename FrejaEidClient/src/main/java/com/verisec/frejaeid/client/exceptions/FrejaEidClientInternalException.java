package com.verisec.frejaeid.client.exceptions;

/**
 * Occurs when client returns an internal error.
 *
 */
public class FrejaEidClientInternalException extends Exception {

    public FrejaEidClientInternalException(String message) {
        super(message);
    }

    public FrejaEidClientInternalException(String message, Throwable cause) {
        super(message, cause);
    }

}
