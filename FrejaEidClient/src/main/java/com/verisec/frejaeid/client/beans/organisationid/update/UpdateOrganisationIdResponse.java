package com.verisec.frejaeid.client.beans.organisationid.update;

import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.general.UpdateStatus;

import java.util.Objects;

public class UpdateOrganisationIdResponse implements FrejaHttpResponse {

    private final UpdateStatus updateStatus;

    public UpdateOrganisationIdResponse(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }

    public UpdateStatus getUpdateStatus() {
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
