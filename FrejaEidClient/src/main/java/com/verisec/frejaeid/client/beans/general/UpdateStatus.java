package com.verisec.frejaeid.client.beans.general;

import java.util.Objects;

public class UpdateStatus {

    private final int added;
    private final int updated;
    private final int deleted;

    public UpdateStatus(int added, int updated, int deleted) {
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
        if (!(o instanceof UpdateStatus)) return false;
        UpdateStatus that = (UpdateStatus) o;
        return added == that.added && updated == that.updated && deleted == that.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(added, updated, deleted);
    }

    @Override
    public String toString() {
        return "UpdateStatus{" +
                "added=" + added +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
