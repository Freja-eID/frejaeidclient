package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author mirko.stojmenovic
 */
public class OriginDeviceDetails {

    private final String frejaCookie;
    private final String userAgent;
    private final String userIpAddress;
    private final FlowType flowType;

    public enum FlowType {
        SAME_DEVICE,
        ANOTHER_DEVICE
    }
    /**
     * Creates instance of {@linkplain OriginDeviceDetails}.
     *
     * @param frejaCookie Cookie to be set and received from end-user browser.
     *                    Each time an initAuthentication is called, the cookie obtained through 
     *                    "Cookie" header of the HTTP request shall be passed to Freja's REST API.
     *                    After transaction is confirmed, a new cookie is received from Freja API, 
     *                    and shall be forwarded to end-user browser via the "Set-Cookie" HTTP header.
     * @return information of end user's device
     */
    public static OriginDeviceDetails create(String frejaCookie) {
        return new OriginDeviceDetails(frejaCookie, null, null, null);
    }

    public static OriginDeviceDetails create(String frejaCookie, String userAgent, String userIpAddress,
                                             FlowType flowType) {
        return new OriginDeviceDetails(frejaCookie, userAgent, userIpAddress, flowType);
    }

    @JsonCreator
    private OriginDeviceDetails(@JsonProperty("frejaCookie") String frejaCookie,
                                @JsonProperty("userAgent") String userAgent,
                                @JsonProperty("userIpAddress") String userIpAddress,
                                @JsonProperty("flowType") FlowType flowType) {
        this.frejaCookie = frejaCookie;
        this.userAgent = userAgent;
        this.userIpAddress = userIpAddress;
        this.flowType = flowType;
    }

    public String getFrejaCookie() {
        return frejaCookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public FlowType getFlowType() {
        return flowType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frejaCookie, userAgent, userIpAddress, flowType);
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
        final OriginDeviceDetails other = (OriginDeviceDetails) obj;
        if (!Objects.equals(this.frejaCookie, other.frejaCookie)) {
            return false;
        }
        if (!Objects.equals(this.userAgent, other.userAgent)) {
            return false;
        }
        if (!Objects.equals(this.userIpAddress, other.userIpAddress)) {
            return false;
        }
        return this.flowType == other.flowType;
    }

    @Override
    public String toString() {
        return "OriginDeviceDetails{" + "frejaCookie=" + frejaCookie + ", userAgent=" + userAgent + ", userIpAddress="
                + userIpAddress + ", flowType=" + flowType + '}';
    }

}
