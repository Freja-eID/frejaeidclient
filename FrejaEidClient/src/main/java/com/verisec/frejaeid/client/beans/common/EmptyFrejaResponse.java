package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EmptyFrejaResponse implements FrejaHttpResponse {

    @JsonIgnore
    public static final EmptyFrejaResponse INSTANCE = new EmptyFrejaResponse();

    public EmptyFrejaResponse() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyFrejaResponse;
    }

    @Override
    public String toString() {
        return "EmptyFrejaResponse{" + '}';
    }

}
