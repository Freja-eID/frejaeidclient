package com.verisec.frejaeid.client.beans.organisationid.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.general.UpdateOrganisationIdStatus;

import java.util.Objects;

public class UpdateOrganisationIdResponse implements FrejaHttpResponse {

    private final UpdateOrganisationIdStatus updateOrganisationIdStatus;

    @JsonCreator
    public UpdateOrganisationIdResponse(
            @JsonProperty("updateOrganisationIdStatus") UpdateOrganisationIdStatus updateOrganisationIdStatus) {
        this.updateOrganisationIdStatus = updateOrganisationIdStatus;
    }

    public UpdateOrganisationIdStatus getUpdateOrganisationIdStatus() {
        return updateOrganisationIdStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOrganisationIdResponse)) return false;
        UpdateOrganisationIdResponse that = (UpdateOrganisationIdResponse) o;
        return Objects.equals(updateOrganisationIdStatus, that.updateOrganisationIdStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updateOrganisationIdStatus);
    }

    @Override
    public String toString() {
        return "UpdateOrganisationIdResponse{" +
                "updateOrganisationIdStatus=" + updateOrganisationIdStatus +
                '}';
    }
}
