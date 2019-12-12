package com.verisec.frejaeid.client.beans.organisationid.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

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
    public String toString() {
        return "InitiateAddOrganisationIdResponse{" + "orgIdRef=" + orgIdRef + '}';
    }
}
