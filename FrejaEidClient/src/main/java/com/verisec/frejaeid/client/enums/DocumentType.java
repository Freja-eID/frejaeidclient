package com.verisec.frejaeid.client.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author veiszec
 **/
public enum DocumentType {

    PASSPORT("PASS","Passport"),
    DRIVING_LICENSE("DRILIC","Driving licence"),
    NATIONAL_ID("NATID","National ID"),
    ID_SIS("IDSIS", "SiS certified ID document"),
    TAX_AGENCY_ID("TAXID","Tax Agency ID card"),
    OTHERID("OTHERID","Other ID"),
    UNKNOWN("UNKNOWN","Unknown document");

    private final String acronym;
    private final String description;

    DocumentType(String acronym, String description) {
        this.acronym = acronym;
        this.description = description;
    }

    @JsonValue
    public String getAcronym() {
        return acronym;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DocumentType createFromJson(String value) {
        try {
            return getByAcronym(value);
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }

    public static DocumentType getByAcronym(String docTypeAcronym) {
        for (DocumentType docType : DocumentType.values()) {
            if (docType.acronym.equals(docTypeAcronym)) {
                return docType;
            }
        }
        return null;
    }
}
