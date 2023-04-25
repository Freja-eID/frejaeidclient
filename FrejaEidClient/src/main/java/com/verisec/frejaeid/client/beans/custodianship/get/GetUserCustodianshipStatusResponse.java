package com.verisec.frejaeid.client.beans.custodianship.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.Objects;

public class GetUserCustodianshipStatusResponse implements FrejaHttpResponse {
    private final String custodianshipStatus;

    @JsonCreator
    public GetUserCustodianshipStatusResponse(@JsonProperty("custodianshipStatus") String custodianshipStatus) {
        this.custodianshipStatus = custodianshipStatus;
    }

    public String getCustodianshipStatus() { return custodianshipStatus; }

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
        final GetUserCustodianshipStatusResponse other = (GetUserCustodianshipStatusResponse) obj;
        if (!Objects.equals(this.custodianshipStatus, other.custodianshipStatus)) {
            return false;
        }
        return  true;

    }

    @Override
    public int hashCode() {
        return Objects.hash(custodianshipStatus);
    }

    @Override
    public String toString() {
        return "GetUserCustodianshipStatusResponse{" +
                "custodianshipStatus='" + custodianshipStatus + '\'' +
                '}';
    }
}
