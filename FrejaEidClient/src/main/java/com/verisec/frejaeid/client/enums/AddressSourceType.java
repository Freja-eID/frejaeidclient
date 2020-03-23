package com.verisec.frejaeid.client.enums;

/**
 *
 * @author vedrbuk
 */
public enum AddressSourceType {
    
    MANUAL_ENTRY("Manual_Entry"),
    GOVERNMENT_REGISTRY("Government_Registry");

    private String sourceType;

    private AddressSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }
  
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public static AddressSourceType getAddressSourceFromSourceName(String addressSourceType) {
        for (AddressSourceType sourceType : values()) {
            if (sourceType.sourceType.equals(addressSourceType)) {
                return sourceType;
            }
        }
        return null;
    }
}
