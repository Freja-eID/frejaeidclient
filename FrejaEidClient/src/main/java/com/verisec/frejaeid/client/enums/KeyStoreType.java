package com.verisec.frejaeid.client.enums;

public enum KeyStoreType {
    JKS("JKS"),
    JCEKS("JCEKS"),
    PKCS12("PKCS12");

    private final String type;

    private KeyStoreType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static String getAllKeyStoreTypes() {
        String allTypes = "";
        for (int i = 0; i < KeyStoreType.values().length - 1; i++) {
            if (i != 0) {
                allTypes += ", ";
            }
            allTypes += KeyStoreType.values()[i];
        }
        allTypes += " and " + KeyStoreType.values()[KeyStoreType.values().length - 1];
        return allTypes;
    }

}
