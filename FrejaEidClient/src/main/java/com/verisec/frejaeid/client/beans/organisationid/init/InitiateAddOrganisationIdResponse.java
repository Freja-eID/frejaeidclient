package com.verisec.frejaeid.client.beans.organisationid.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.Objects;

public class InitiateAddOrganisationIdResponse implements FrejaHttpResponse {

    private final String orgIdRef;
    private final String qrCodeSecret;

    @JsonCreator
    public InitiateAddOrganisationIdResponse(@JsonProperty(value = "orgIdRef") String orgIdRef,
                                             @JsonProperty(value = "qrCodeSecret") String qrCodeSecret) {
        this.orgIdRef = orgIdRef;
        this.qrCodeSecret = qrCodeSecret;
    }

    public String getOrgIdRef() {
        return orgIdRef;
    }

    public String getQrCodeSecret() {
        return qrCodeSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitiateAddOrganisationIdResponse)) return false;
        InitiateAddOrganisationIdResponse that = (InitiateAddOrganisationIdResponse) o;
        return Objects.equals(orgIdRef, that.orgIdRef) &&
                Objects.equals(qrCodeSecret, that.qrCodeSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgIdRef, qrCodeSecret);
    }

    @Override
    public String toString() {
        return "InitiateAddOrganisationIdResponse{" +
                "orgIdRef='" + orgIdRef + '\'' +
                ", qrCodeSecret='" + qrCodeSecret + '\'' +
                '}';
    }
}
