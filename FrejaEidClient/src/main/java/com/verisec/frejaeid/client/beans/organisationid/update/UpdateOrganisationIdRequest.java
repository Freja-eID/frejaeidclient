package com.verisec.frejaeid.client.beans.organisationid.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationIdAttribute;

import java.util.List;
import java.util.Objects;

public class UpdateOrganisationIdRequest implements RelyingPartyRequest {

    private final String identifier;
    private final List<OrganisationIdAttribute> additionalAttributes;
    private final String relyingPartyId;


    public UpdateOrganisationIdRequest(String identifier, List<OrganisationIdAttribute> additionalAttributes) {
        this.identifier = identifier;
        this.additionalAttributes = additionalAttributes;
        this.relyingPartyId = null;
    }

    public UpdateOrganisationIdRequest(String identifier, List<OrganisationIdAttribute> additionalAttributes,
                                       String relyingPartyId) {
        this.identifier = identifier;
        this.additionalAttributes = additionalAttributes;
        this.relyingPartyId = relyingPartyId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<OrganisationIdAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOrganisationIdRequest)) return false;
        UpdateOrganisationIdRequest that = (UpdateOrganisationIdRequest) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(additionalAttributes, that.additionalAttributes) &&
                Objects.equals(relyingPartyId, that.relyingPartyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, additionalAttributes, relyingPartyId);
    }

    @Override
    public String toString() {
        return "UpdateOrganisationIdRequest{" +
                "identifier='" + identifier + '\'' +
                ", additionalAttributes=" + additionalAttributes +
                ", relyingPartyId='" + relyingPartyId + '\'' +
                '}';
    }
}
