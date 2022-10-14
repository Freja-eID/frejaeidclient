package com.verisec.frejaeid.client.enums;

public enum SignatureType {

    SIMPLE("SIMPLE"),
    EXTENDED("EXTENDED"),
    CMS_EXPLICIT("CMS_EXPLICIT");

    private final String name;

    private SignatureType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SignatureType getByName(String name) {
        for (SignatureType transactionStatus : SignatureType.values()) {
            if (transactionStatus.name.equals(name)) {
                return transactionStatus;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "SignatureType{" + "name=" + name + '}';
    }
}
