package com.verisec.frejaeid.client.enums;

public enum UserConfirmationMethod {

    DEFAULT("DEFAULT"),
    DEFAULT_AND_FACE("DEFAULT_AND_FACE");

    private final String type;

    UserConfirmationMethod(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
}
