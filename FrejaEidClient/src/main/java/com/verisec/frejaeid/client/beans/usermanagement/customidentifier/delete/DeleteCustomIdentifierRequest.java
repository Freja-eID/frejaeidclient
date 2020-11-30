package com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.DeleteIdentifierRequest;

public class DeleteCustomIdentifierRequest extends DeleteIdentifierRequest {

    /**
     * Returns instance of {@linkplain DeleteCustomIdentifierRequest} with given
     * unique customIdentifier per relying party.
     *
     * @param customIdentifier identifier to be deleted for the end user. It
     *                         cannot be {@code null} or empty.
     * @return request
     */
    public static DeleteCustomIdentifierRequest create(String customIdentifier) {
        return new DeleteCustomIdentifierRequest(customIdentifier);
    }

    /**
     * Returns instance of {@linkplain DeleteCustomIdentifierRequest} with given
     * unique customIdentifier per relying party.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param customIdentifier identifier to be deleted for the end user. It
     *                         cannot be {@code null} or empty.
     * @param relyingPartyId   specifies relying party id by which custom
     *                         identifier will be deleted. It cannot be {@code null} or empty.
     * @return request
     */
    public static DeleteCustomIdentifierRequest create(String customIdentifier, String relyingPartyId) {
        return new DeleteCustomIdentifierRequest(customIdentifier, relyingPartyId);
    }

    @JsonCreator
    private DeleteCustomIdentifierRequest(@JsonProperty(value = "customIdentifier") String customIdentifier) {
        super(customIdentifier);
    }

    private DeleteCustomIdentifierRequest(String customIdentifier, String relyingPartyId) {
        super(customIdentifier, relyingPartyId);
    }

    @JsonProperty(value = "customIdentifier")
    public String getCustomIdentifier() {
        return super.getIdentifier();
    }

    @Override
    public String toString() {
        return "DeleteCustomIdentifierRequest{" + super.toString() + '}';
    }
}
