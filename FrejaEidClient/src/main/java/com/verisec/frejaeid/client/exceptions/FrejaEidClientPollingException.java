package com.verisec.frejaeid.client.exceptions;

/**
 * Occurs when sending request has reached polling timeout. Timeout can be set
 * per client.
 *
 */
public class FrejaEidClientPollingException extends Exception {

    public FrejaEidClientPollingException(String message) {
        super(message);
    }

}
