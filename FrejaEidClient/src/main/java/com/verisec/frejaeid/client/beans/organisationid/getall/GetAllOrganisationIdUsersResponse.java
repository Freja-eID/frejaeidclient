package com.verisec.frejaeid.client.beans.organisationid.getall;

import com.verisec.frejaeid.client.beans.general.OrganisationIdUserInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import java.util.List;
import java.util.Objects;

public class GetAllOrganisationIdUsersResponse implements FrejaHttpResponse {

    private final List<OrganisationIdUserInfo> userInfos;

    @JsonCreator
    public GetAllOrganisationIdUsersResponse(@JsonProperty("userInfos") List<OrganisationIdUserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public List<OrganisationIdUserInfo> getUserInfos() {
        return userInfos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfos);
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
        final GetAllOrganisationIdUsersResponse other = (GetAllOrganisationIdUsersResponse) obj;
        if (!Objects.equals(this.userInfos, other.userInfos)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetAllOrganisationIdUsersResponse{" + "userInfos=" + userInfos + '}';
    }

}
