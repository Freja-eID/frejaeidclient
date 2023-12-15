package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UpdateOrganisationIdStatus {

    private final int added;
    private final int updated;
    private final int deleted;

    @JsonCreator
    public UpdateOrganisationIdStatus(@JsonProperty("added") int added,
                                      @JsonProperty("updated") int updated,
                                      @JsonProperty("deleted") int deleted) {
        this.added = added;
        this.updated = updated;
        this.deleted = deleted;
    }

    public int getAdded() {
        return added;
    }

    public int getUpdated() {
        return updated;
    }

    public int getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateOrganisationIdStatus)) return false;
        UpdateOrganisationIdStatus that = (UpdateOrganisationIdStatus) o;
        return added == that.added && updated == that.updated && deleted == that.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(added, updated, deleted);
    }

    @Override
    public String toString() {
        return "UpdateOrganisationIdStatus{" +
                "added=" + added +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
