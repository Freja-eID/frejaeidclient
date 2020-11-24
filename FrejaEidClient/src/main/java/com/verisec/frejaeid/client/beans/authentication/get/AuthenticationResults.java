package com.verisec.frejaeid.client.beans.authentication.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.List;
import java.util.Objects;

public class AuthenticationResults implements FrejaHttpResponse {

    private final List<AuthenticationResult> authenticationResults;

    @JsonCreator
    public AuthenticationResults(@JsonProperty(value = "authenticationResults")
                                         List<AuthenticationResult> authenticationResults) {
        this.authenticationResults = authenticationResults;
    }

    public List<AuthenticationResult> getAuthenticationResults() {
        return authenticationResults;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authenticationResults);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthenticationResults other = (AuthenticationResults) obj;
        if (!Objects.equals(this.authenticationResults, other.authenticationResults)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuthenticationResults{" + "authenticationResults = " + authenticationResults + '}';
    }

}
