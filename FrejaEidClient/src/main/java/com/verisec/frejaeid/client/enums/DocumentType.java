package com.verisec.frejaeid.client.enums;

/**
 * @author veiszec
 **/
public enum DocumentType {

    PASSPORT("PASS","Passport"),
    DRIVING_LICENSE("DRILIC","Driving licence"),
    NATIONAL_ID("NATID","National ID"),
    ID_SIS("IDSIS", "SiS certified ID document"),
    TAX_AGENCY_ID("TAXID","Tax Agency ID card"),
    OTHERID("OTHERID","Other ID");

    private final String acronym;
    private final String description;

    DocumentType(String acronym, String description) {
        this.acronym = acronym;
        this.description = description;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getDescription() {
        return description;
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
