package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

/**
 * Contains information regarding organisation ID and its issuer.
 */
public class OrganisationIdInfo {

    private final String identifier;
    private final Map<String, String> issuerFriendlyName;
    private final String issuerCode;

    /**
     * Creates instance of {@linkplain OrganisationIdInfo}.
     *
     * @param identifier         User's Organisation ID. It cannot be {@code null} or empty.
     * @param issuerFriendlyName Friendly name of the organisation which issued the user's Organisation ID
     *                           in Swedish and English. It cannot be {@code null} or empty.
     * @param issuerCode         Unique identifier of the organisation which issued the user's Organisation ID. It
     *                           can be {@code null} or empty.
     */
    @JsonCreator
    public OrganisationIdInfo(@JsonProperty("identifier") String identifier,
                              @JsonProperty("issuerFriendlyName") Map<String, String> issuerFriendlyName,
                              @JsonProperty("issuerCode") String issuerCode) {
        this.identifier = identifier;
        this.issuerFriendlyName = issuerFriendlyName;
        this.issuerCode = issuerCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Map<String, String> getIssuerFriendlyName() {
        return issuerFriendlyName;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationIdInfo that = (OrganisationIdInfo) o;
        return Objects.equals(identifier, that.identifier)
                && Objects.equals(issuerFriendlyName, that.issuerFriendlyName)
                && Objects.equals(issuerCode, that.issuerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, issuerFriendlyName, issuerCode);
    }

    @Override
    public String toString() {
        return "OrganisationIdInfo{" +
                "identifier='" + identifier + '\'' +
                ", issuerFriendlyName=" + issuerFriendlyName +
                ", issuerCode='" + issuerCode + '\'' +
                '}';
    }
}
