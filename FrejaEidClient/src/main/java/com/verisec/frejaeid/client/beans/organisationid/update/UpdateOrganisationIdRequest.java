package com.verisec.frejaeid.client.beans.organisationid.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationIdAttribute;

import java.util.List;
import java.util.Objects;

public class UpdateOrganisationIdRequest implements RelyingPartyRequest {

    private final String identifier;
    private final List<OrganisationIdAttribute> additionalAttributes;
    private final String relyingPartyId;

    public static UpdateOrganisationIdRequest create(String identifier, List<OrganisationIdAttribute> additionalAttributes){
        return new UpdateOrganisationIdRequest(identifier, additionalAttributes);
    }

    public static UpdateOrganisationIdRequest create(String identifier, List<OrganisationIdAttribute> additionalAttributes, String relyingPartyId){
        return new UpdateOrganisationIdRequest(identifier, additionalAttributes, relyingPartyId);
    }

    /**
     * Returns instance of {@linkplain UpdateOrganisationIdRequest}
     *
     * @param identifier           identifier to be updated for the end user. It cannot be
     *                             {@code null} or empty.
     * @param additionalAttributes additional attributes related to the identifier.
     *                             It can be {@code null} or empty.
     * @return request
     */
    @JsonCreator
    private UpdateOrganisationIdRequest(@JsonProperty("identifier") String identifier,
                                        @JsonProperty("additionalAttributes") List<OrganisationIdAttribute> additionalAttributes) {
        this.identifier = identifier;
        this.additionalAttributes = additionalAttributes;
        this.relyingPartyId = null;
    }

    /**
     * Returns instance of {@linkplain UpdateOrganisationIdRequest}
     *
     * @param identifier            identifier to be updated for the end user. It cannot be
     *                              {@code null} or empty.
     * @param additionalAttributes  additional attributes related to the identifier.
     *                              It can be {@code null} or empty.
     * @param relyingPartyId        specifies relying party id for which transaction is
     *                              initiated. It cannot be {@code null} or empty.
     * @return request
     */
    private UpdateOrganisationIdRequest(String identifier, List<OrganisationIdAttribute> additionalAttributes,
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
