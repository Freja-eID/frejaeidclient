package com.verisec.frejaeid.client.beans.organisationid.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.Result;
import com.verisec.frejaeid.client.enums.TransactionStatus;

/**
 * Contains transaction reference, transaction status, details and freja cookie.
 */
public class OrganisationIdResult extends Result {

    @JsonCreator
    public OrganisationIdResult(@JsonProperty(value = "orgIdRef") String reference,
                                @JsonProperty(value = "status") TransactionStatus status,
                                @JsonProperty(value = "details") String details,
                                @JsonProperty(value = "frejaCookie") String frejaCookie) {
        super(reference, status, details, null, frejaCookie);
    }

    /**
     * @return A unique organisation id transaction reference.
     */
    @JsonProperty(value = "orgIdRef")
    public String getOrgIdRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "OrganisationIdResult{" + super.toString() + '}';
    }
}
