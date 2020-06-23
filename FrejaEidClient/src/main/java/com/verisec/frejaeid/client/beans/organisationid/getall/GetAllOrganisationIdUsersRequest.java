package com.verisec.frejaeid.client.beans.organisationid.getall;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import java.util.Objects;

/**
 *
 * @author vedrbuk
 */
public class GetAllOrganisationIdUsersRequest implements RelyingPartyRequest {

    @JsonIgnore
    private final String relyingPartyId;

    private GetAllOrganisationIdUsersRequest(String relyingPartyId) {
        this.relyingPartyId = relyingPartyId;
    }

    /**
     * Returns instance of {@linkplain GetAllOrganisationIdUsersRequest}.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param relyingPartyId specifies relying party id for which response
     * should be returned. It cannot be {@code null} or empty.
     *
     * @return request
     */
    public static GetAllOrganisationIdUsersRequest create(String relyingPartyId) {
        return new GetAllOrganisationIdUsersRequest(relyingPartyId);
    }

    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relyingPartyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GetAllOrganisationIdUsersRequest other = (GetAllOrganisationIdUsersRequest) obj;
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetAllOrganisationIdUsersRequest{" + "relyingPartyId=" + relyingPartyId + '}';
    }

}
