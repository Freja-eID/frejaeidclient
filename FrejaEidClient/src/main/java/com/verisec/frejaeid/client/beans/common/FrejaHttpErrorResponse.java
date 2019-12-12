package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FrejaHttpErrorResponse implements FrejaHttpResponse {

    private final int code;
    private final String message;

    @JsonCreator
    public FrejaHttpErrorResponse(@JsonProperty(value = "code") int code, @JsonProperty(value = "message") String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
