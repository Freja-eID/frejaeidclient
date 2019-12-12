package com.verisec.frejaeid.client.enums;

public enum DataToSignType {

    SIMPLE_UTF8_TEXT("SIMPLE_UTF8_TEXT"),
    EXTENDED_UTF8_TEXT("EXTENDED_UTF8_TEXT");

    private final String type;

    private DataToSignType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DataToSignType getByType(String type) {
        for (DataToSignType dataToSignType : DataToSignType.values()) {
            if (dataToSignType.getType().equals(type)) {
                return dataToSignType;
            }
        }
        return null;
    }
}
