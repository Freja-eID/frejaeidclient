package com.verisec.frejaeid.client.beans.organisationid.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.Objects;

public class InitiateAddOrganisationIdResponse implements FrejaHttpResponse {

    private final String orgIdRef;

    @JsonCreator
    public InitiateAddOrganisationIdResponse(@JsonProperty(value = "orgIdRef") String orgIdRef) {
        this.orgIdRef = orgIdRef;
    }

    public String getOrgIdRef() {
        return orgIdRef;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitiateAddOrganisationIdResponse)) return false;
        InitiateAddOrganisationIdResponse that = (InitiateAddOrganisationIdResponse) o;
        return Objects.equals(orgIdRef, that.orgIdRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgIdRef);
    }

    @Override
    public String toString() {
        return "InitiateAddOrganisationIdResponse{" +
                "orgIdRef='" + orgIdRef + '\'' +
                '}';
    }
}
