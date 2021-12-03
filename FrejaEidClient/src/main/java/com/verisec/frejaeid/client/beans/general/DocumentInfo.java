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

    private final DocumentType documentType;
    private final String serialNumber;
    private final Country country;
    /**
     * Document expiration date in YYYY-MM-DD format
     */
    private final String expirationDate;

    @JsonCreator
    public DocumentInfo(@JsonProperty("documentType") DocumentType documentType,
                        @JsonProperty("serialNumber") String serialNumber,
                        @JsonProperty("country") Country country,
                        @JsonProperty("expirationDate") String expirationDate) {
        this.documentType = documentType;
        this.serialNumber = serialNumber;
        this.country = country;
        this.expirationDate = expirationDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
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
        return documentType == that.documentType && Objects.equals(serialNumber, that.serialNumber) && country == that.country && Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentType, serialNumber, country, expirationDate);
    }
}
