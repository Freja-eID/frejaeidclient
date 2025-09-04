package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.RegistrationState;

import java.util.Objects;

public class OrganisationIdUserInfo {

    private final OrganisationId organisationId;
    private final SsnUserInfo ssn;
    private final RegistrationState registrationState;
    private final String uniquePersonalIdentifier;

    @JsonCreator
    public OrganisationIdUserInfo(@JsonProperty("organisationId") OrganisationId organisationId,
                                  @JsonProperty("ssn") SsnUserInfo ssn,
                                  @JsonProperty("registrationState") RegistrationState registrationState,
                                  @JsonProperty("uniquePersonalIdentifier") String uniquePersonalIdentifier) {
        this.organisationId = organisationId;
        this.ssn = ssn;
        this.registrationState = registrationState;
        this.uniquePersonalIdentifier = uniquePersonalIdentifier;
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

    public String getUniquePersonalIdentifier() {
        return uniquePersonalIdentifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(organisationId, ssn, registrationState, uniquePersonalIdentifier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganisationIdUserInfo)) return false;
        OrganisationIdUserInfo that = (OrganisationIdUserInfo) o;
        return Objects.equals(organisationId, that.organisationId) &&
                Objects.equals(ssn, that.ssn) &&
                registrationState == that.registrationState &&
                Objects.equals(uniquePersonalIdentifier, that.uniquePersonalIdentifier);
    }

    @Override
    public String toString() {
        return "OrganisationIdUserInfo{" +
                "organisationId=" + organisationId +
                ", ssn=" + ssn +
                ", registrationState=" + registrationState +
                ", uniquePersonalIdentifier='" + uniquePersonalIdentifier + '\'' +
                '}';
    }
}
