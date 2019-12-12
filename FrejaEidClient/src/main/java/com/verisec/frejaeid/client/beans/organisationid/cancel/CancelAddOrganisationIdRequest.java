package com.verisec.frejaeid.client.beans.organisationid.cancel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.CancelRequest;

public class CancelAddOrganisationIdRequest extends CancelRequest {

    /**
     * Returns instance of {@linkplain CancelAddOrganisationIdRequest} with
     * given unique reference.
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static CancelAddOrganisationIdRequest create(String reference) {
        return new CancelAddOrganisationIdRequest(reference);
    }

    /**
     * Returns instance of {@linkplain CancelAddOrganisationIdRequest} with
     * given unique reference and relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static CancelAddOrganisationIdRequest create(String reference, String relyingPartyId) {
        return new CancelAddOrganisationIdRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private CancelAddOrganisationIdRequest(@JsonProperty(value = "orgIdRef") String reference) {
        super(reference);
    }

    private CancelAddOrganisationIdRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "orgIdRef")
    public String getOrgIdRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "CancelAddOrganisationIdRequest{" + super.toString() + '}';
    }

}
