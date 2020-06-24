package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.RegistrationState;
import java.util.Objects;

public class OrganisationIdUserInfo {

    private final OrganisationId organisationId;
    private final SsnUserInfo ssn;
    private final RegistrationState registrationState;

    @JsonCreator
    public OrganisationIdUserInfo(@JsonProperty("organisationId") OrganisationId organisationId, 
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
    public int hashCode() {
        return Objects.hash(organisationId,ssn, registrationState);
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
        final OrganisationIdUserInfo other = (OrganisationIdUserInfo) obj;
        if (!Objects.equals(this.organisationId, other.organisationId)) {
            return false;
        }
        if (!Objects.equals(this.ssn, other.ssn)) {
            return false;
        }
        if (this.registrationState != other.registrationState) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrganisationIdUserInfo{" + "organisationId=" + organisationId + ", ssn=" + ssn + ", registrationState=" + registrationState + '}';
    }
    
}
