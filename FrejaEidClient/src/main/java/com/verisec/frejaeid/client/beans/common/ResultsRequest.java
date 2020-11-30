package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public abstract class ResultsRequest implements RelyingPartyRequest {

    private final IncludePrevious includePrevious;
    private final String relyingPartyId;

    public ResultsRequest(String relyingPartyId) {
        this(IncludePrevious.ALL, relyingPartyId);
    }

    @JsonCreator
    private ResultsRequest(@JsonProperty(value = "includePrevious") IncludePrevious includePrevious) {
        this(includePrevious, null);
    }

    private ResultsRequest(IncludePrevious includePrevious, String relyingPartyId) {
        this.includePrevious = includePrevious;
        this.relyingPartyId = relyingPartyId;
    }

    @JsonProperty(value = "includePrevious")
    public IncludePrevious getIncludePrevious() {
        return includePrevious;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(includePrevious, relyingPartyId);
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
        final ResultsRequest other = (ResultsRequest) obj;
        if (!Objects.equals(this.includePrevious, other.includePrevious)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResultsRequest{" + "includePrevious=" + includePrevious + '}';
    }

    private static enum IncludePrevious {

        ALL;

    }

}
