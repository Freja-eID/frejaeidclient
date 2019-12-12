package com.verisec.frejaeid.client.beans.organisationid.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.ResultRequest;

public class OrganisationIdResultRequest extends ResultRequest {

    /**
     * Returns instance of {@linkplain OrganisationIdResultRequest} with given
     * unique reference.
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static OrganisationIdResultRequest create(String reference) {
        return new OrganisationIdResultRequest(reference);
    }

    /**
     * Returns instance of {@linkplain OrganisationIdResultRequest} with given
     * unique reference and relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static OrganisationIdResultRequest create(String reference, String relyingPartyId) {
        return new OrganisationIdResultRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private OrganisationIdResultRequest(@JsonProperty(value = "orgIdRef") String reference) {
        super(reference, null);
    }

    private OrganisationIdResultRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "orgIdRef")
    public String getOrgIdRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "OrganisationIdResultRequest{" + super.toString() + '}';
    }
}
