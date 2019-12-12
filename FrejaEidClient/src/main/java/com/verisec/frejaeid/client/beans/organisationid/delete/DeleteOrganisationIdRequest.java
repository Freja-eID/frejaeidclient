package com.verisec.frejaeid.client.beans.organisationid.delete;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.DeleteIdentifierRequest;

public class DeleteOrganisationIdRequest extends DeleteIdentifierRequest {

    /**
     * Returns instance of {@linkplain DeleteOrganisationIdRequest} with given
     * unique identifier per relying party.
     *
     * @param identifier identifier to be deleted for the end user. It cannot be
     * {@code null} or empty.
     *
     * @return request
     */
    public static DeleteOrganisationIdRequest create(String identifier) {
        return new DeleteOrganisationIdRequest(identifier);
    }

    /**
     * Returns instance of {@linkplain DeleteOrganisationIdRequest} with given
     * unique identifier per relying party.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param identifier identifier to be deleted for the end user. It cannot be
     * {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     *
     * @return request
     */
    public static DeleteOrganisationIdRequest create(String identifier, String relyingPartyId) {
        return new DeleteOrganisationIdRequest(identifier, relyingPartyId);
    }

    @JsonCreator
    private DeleteOrganisationIdRequest(@JsonProperty(value = "identifier") String identifier) {
        super(identifier);
    }

    private DeleteOrganisationIdRequest(String identifier, String relyingPartyId) {
        super(identifier, relyingPartyId);
    }

    @JsonProperty(value = "identifier")
    public String getIdentifier() {
        return super.getIdentifier();
    }

    @Override
    public String toString() {
        return "DeleteOrganisationIdRequest{" + super.toString() + '}';
    }

}
