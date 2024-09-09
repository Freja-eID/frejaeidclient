package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NetworkInfo {

    private final String publicIp;

    public NetworkInfo(@JsonProperty(value = "publicIp") String publicIp) {
        this.publicIp = publicIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkInfo)) return false;
        NetworkInfo that = (NetworkInfo) o;
        return Objects.equals(publicIp, that.publicIp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(publicIp);
    }

    @Override
    public String toString() {
        return "NetworkInfo{" + "publicIp=" + publicIp + '}';
    }
}
