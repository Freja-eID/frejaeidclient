package com.verisec.frejaeid.client.beans.organisationid.update;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.general.UpdateOrganisationIdStatus;

import java.util.Objects;

public class UpdateOrganisationIdResponse implements FrejaHttpResponse {

    private final UpdateOrganisationIdStatus updateStatus;

    @JsonCreator
    public UpdateOrganisationIdResponse(
            @JsonProperty("updateStatus") UpdateOrganisationIdStatus updateStatus) {
        this.updateStatus = updateStatus;
    }

    public UpdateOrganisationIdStatus getUpdateStatus() {
        return updateStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOrganisationIdResponse)) return false;
        UpdateOrganisationIdResponse that = (UpdateOrganisationIdResponse) o;
        return Objects.equals(updateStatus, that.updateStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updateStatus);
    }

    @Override
    public String toString() {
        return "UpdateOrganisationIdResponse{" +
                "updateStatus=" + updateStatus +
                '}';
    }
}
