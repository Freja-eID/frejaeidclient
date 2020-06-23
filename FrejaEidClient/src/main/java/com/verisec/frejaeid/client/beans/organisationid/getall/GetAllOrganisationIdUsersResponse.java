package com.verisec.frejaeid.client.beans.organisationid.getall;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.RegistrationState;

/**
 *
 * @author vedrbuk
 */
public class GetAllOrganisationIdUsersResponse implements FrejaHttpResponse {

    private final OrganisationId organisationId;
    private final SsnUserInfo ssn;
    private final RegistrationState registrationState;

    @JsonCreator
    public GetAllOrganisationIdUsersResponse(@JsonProperty("organisationId") OrganisationId organisationId, 
            @JsonProperty("ssn") SsnUserInfo ssn, @JsonProperty("registrationState") RegistrationState registrationState) {
        this.organisationId = organisationId;
        this.ssn = ssn;
        this.registrationState = registrationState;
    }

    public OrganisationId getOrganisationId() {
        return organisationId;
    }

    public SsnUserInfo getSsn() {
        return ssn;
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    @Override
    public String toString() {
        return "GetAllOrganisationIdUsersResponse{" + "organisationId=" + organisationId + ", ssn=" + ssn + ", registrationState=" + registrationState + '}';
    }
    
}
