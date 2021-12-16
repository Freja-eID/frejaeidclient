package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.DocumentType;

import java.util.Objects;

/**
 * DocumentInfo contains information about the user's physical document. It can be requested as
 * an attribute.
 */
public class DocumentInfo {

    private final DocumentType type;
    private final String serialNumber;
    private final Country country;
    /**
     * Document expiration date in YYYY-MM-DD format
     */
    private final String expirationDate;

    @JsonCreator
    public DocumentInfo(@JsonProperty("type") DocumentType type,
                        @JsonProperty("serialNumber") String serialNumber,
                        @JsonProperty("country") Country country,
                        @JsonProperty("expirationDate") String expirationDate) {
        this.type = type;
        this.serialNumber = serialNumber;
        this.country = country;
        this.expirationDate = expirationDate;
    }

    public DocumentType getType() {
        return type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Country getCountry() {
        return country;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentInfo that = (DocumentInfo) o;
        return type == that.type && Objects.equals(serialNumber, that.serialNumber) && country == that.country && Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, serialNumber, country, expirationDate);
    }

    @Override
    public String toString() {
        return "DocumentInfo{" +
                "documentType=" + type +
                ", serialNumber='" + serialNumber + '\'' +
                ", country=" + country +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
