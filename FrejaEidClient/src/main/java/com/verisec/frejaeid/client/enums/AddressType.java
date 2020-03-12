package com.verisec.frejaeid.client.enums;

/**
 *
 * @author vedrbuk
 */
public enum AddressType {

    RESIDENTIAL("Residential"),
    POSTAL("Postal");

    private String type;

    private AddressType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static AddressType getByType(String addressType) {
        for (AddressType type : values()) {
            if (type.type.equals(addressType)) {
                return type;
            }
        }
        return null;
    }

}
