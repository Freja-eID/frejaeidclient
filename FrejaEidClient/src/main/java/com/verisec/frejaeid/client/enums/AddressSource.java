package com.verisec.frejaeid.client.enums;

/**
 *
 * @author vedrbuk
 */
public enum AddressSource {
    
    MANUAL_ENTRY("Manual_Entry"),
    GOVERNMENT_REGISTRY("Government_Registry");

    private String source;

    public String getSourceTypeName() {
        return source;
    }
  
    public void setSourceTypeName(String source) {
        this.source = source;
    }

    private AddressSource(String source) {
        this.source = source;
    }

    public static AddressSource getAddressSourceFromSourceName(String addressSource) {
        for (AddressSource source : values()) {
            if (source.source.equals(addressSource)) {
                return source;
            }
        }
        return null;
    }
}
