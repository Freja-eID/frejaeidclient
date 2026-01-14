package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.DocumentType;

import java.util.Objects;

public class DocumentInfoWithPdf {
    private final SsnUserInfo ssn;
    private final BasicUserInfo basicUserInfo;
    private final String dateOfBirth;
    private final String gender;
    private final DocumentType type;
    private final Country country;
    private final String serialNumber;
    private final String expirationDate;
    private final String pdf;
    private final String documentPhoto;
    private final String nfcIdPhoto;

    @JsonCreator
    public DocumentInfoWithPdf(
            @JsonProperty("ssn") SsnUserInfo ssn,
            @JsonProperty("basicUserInfo") BasicUserInfo basicUserInfo,
            @JsonProperty("dateOfBirth") String dateOfBirth,
            @JsonProperty("gender") String gender,
            @JsonProperty("type") DocumentType type,
            @JsonProperty("serialNumber") String serialNumber,
            @JsonProperty("country") Country country,
            @JsonProperty("expirationDate") String expirationDate,
            @JsonProperty("pdf") String pdf,
            @JsonProperty("documentPhoto") String documentPhoto,
            @JsonProperty("nfcIdPhoto") String nfcIdPhoto) {
        this.ssn = ssn;
        this.basicUserInfo = basicUserInfo;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.type = type;
        this.serialNumber = serialNumber;
        this.country = country;
        this.expirationDate = expirationDate;
        this.pdf = pdf;
        this.documentPhoto = documentPhoto;
        this.nfcIdPhoto = nfcIdPhoto;
    }

    public SsnUserInfo getSsn() {
        return ssn;
    }

    public BasicUserInfo getBasicUserInfo() {
        return basicUserInfo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public DocumentType getType() {
        return type;
    }

    public Country getCountry() {
        return country;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getPdf() {
        return pdf;
    }

    public String getDocumentPhoto() {
        return documentPhoto;
    }

    public String getNfcIdPhoto() {
        return nfcIdPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentInfoWithPdf that = (DocumentInfoWithPdf) o;
        return Objects.equals(ssn, that.ssn) &&
                Objects.equals(basicUserInfo, that.basicUserInfo) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(gender, that.gender) &&
                type == that.type &&
                country == that.country &&
                Objects.equals(serialNumber, that.serialNumber) &&
                Objects.equals(expirationDate, that.expirationDate) &&
                Objects.equals(pdf, that.pdf) &&
                Objects.equals(documentPhoto, that.documentPhoto) &&
                Objects.equals(nfcIdPhoto, that.nfcIdPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssn, basicUserInfo, dateOfBirth, gender,
                type, country, serialNumber, expirationDate, pdf, documentPhoto, nfcIdPhoto);
    }
}
