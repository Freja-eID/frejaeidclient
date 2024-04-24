package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.DocumentType;

import java.util.Objects;

public class DocumentInfoWithPdf extends DocumentInfo {
    /**
     * PDF file as a Base64-encoded String
     */
    private final String pdf;

    @JsonCreator
    public DocumentInfoWithPdf(@JsonProperty("type") DocumentType type,
                               @JsonProperty("serialNumber") String serialNumber,
                               @JsonProperty("country") Country country,
                               @JsonProperty("expirationDate") String expirationDate,
                               @JsonProperty("pdf") String pdf) {
        super(type, serialNumber, country, expirationDate);
        this.pdf = pdf;
    }

    public String getPdf() {
        return pdf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentInfoWithPdf that = (DocumentInfoWithPdf) o;
        return Objects.equals(pdf, that.pdf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pdf);
    }

    @Override
    public String toString() {
        return "DocumentInfo{" +
                "documentType=" + super.getType() +
                ", serialNumber='" + super.getSerialNumber() + '\'' +
                ", country=" + super.getCountry() +
                ", expirationDate='" + super.getExpirationDate() + '\'' +
                ", pdf=" + pdf +
                '}';
    }

}
