package com.verisec.frejaeid.client.enums;

public enum FrejaEidErrorCode {

    //General/common errors
    INTERNAL_ERROR(0, "Internal error."),
    INVALID_PARAMETER(1000, "Invalid parameters."),
    INVALID_USER_INFO_TYPE(1001, "Invalid or missing userInfoType."),
    INVALID_USER_INFO(1002, "Invalid or missing userInfo."),
    INVALID_RESTRICT(1003, "Invalid restrict."),
    METHOD_NOT_ALLOWED_FOR_RELYING_PARTY(1004, "You are not allowed to call this method."),
    SERVICE_NOT_ENABLED(1005, "User has disabled your service."),
    INVALID_CONFIDENTIAL(1006, "Invalid confidential."),
    INVALID_MIN_REGISTRATION_LEVEL(1007, "Invalid minimum registration level."),
    UNKNOWN_RELYING_PARTY(1008, "Unknown Relying party."),
    INTEGRATOR_SPECIFIC_USER_ID_PARAMETER_NOT_ALLOWED(1009, "You are not allowed to request integrator specific user " +
            "id parameter."),
    INVALID_JSON_REQUEST(1010, "JSON request cannot be parsed."),
    INVALID_RELYING_PARTY_ID(1011, "Invalid relyingPartyId."),
    NON_EXISTING_USER(1012, "User with the specified user info does not exist in the Freja eID database."),
    INVALID_REFERENCE(1100, "Invalid reference (for example, nonexistent or expired)."),
    INVALID_INCLUDE_PREVIOUS(1200, "Invalid or missing includePrevious parameter."),
    //Authentication errors
    AUTH_INIT_FAILED(2000, "Authentication request failed. Previous authentication request was rejected due to " +
            "security reasons."),
    AUTH_INIT_INVALID_ATTRIBUTES_TO_RETURN(2002, "Invalid attributesToReturn parameter."),
    AUTH_INIT_FAILED_TO_GET_CUSTOM_IDENTIFIER(2003, "Custom identifier has to exist when it is requested."),
    //Signing errors
    SIGN_INIT_INVALID_DATA_TO_SIGN_TYPE(3000, "Invalid or missing dataToSignType."),
    SIGN_INIT_INVALID_DATA_TO_SIGN(3001, "Invalid or missing dataToSign."),
    SIGN_INIT_INVALID_SIGNATURE_TYPE(3002, "Invalid or missing signatureType."),
    SIGN_INIT_INVALID_EXPIRY(3003, "Invalid expiry."),
    SIGN_INIT_INVALID_PUSH_NOTIFICATION(3004, "Invalid push notification."),
    SIGN_INIT_INVALID_ATTRIBUTES_TO_RETURN(3005, "Invalid attributesToReturn parameter."),
    SIGN_INIT_FAILED_TO_GET_CUSTOM_IDENTIFIER(3006, "Custom identifier has to exist when it is requested."),
    SIGN_INIT_INVALID_TITLE(3007, "Invalid title."),
    //organisation id
    ORGANISATION_ID_INVALID_IDENTIFIER(4000, "Invalid or missing organisation id identifier."),
    ORGANISATION_ID_IDENTIFIER_DOES_NOT_EXIST(4001, "There is no user for given organisation id identifier."),
    ORGANISATION_ID_IDENTIFIER_ALREADY_EXIST(4002, "This organisation id identifier is already used."),
    ORGANISATION_ID_INVALID_EXPIRY(4003, "Invalid expiry."),
    ORGANISATION_ID_INVALID_TITLE(4004, "Invalid or missing organisation id title."),
    ORGANISATION_ID_INVALID_IDENTIFIER_NAME(4005, "Invalid or missing organisation id identifier name."),
    //user management
    INVALID_CUSTOM_IDENTIFIER(5000, "Invalid or missing customIdentifier."),
    USER_MANAGEMENT_CUSTOM_IDENTIFIER_DOES_NOT_EXIST(5001, "There is no user for given custom identifier."),
    USER_MANAGEMENT_CUSTOM_IDENTIFIER_ALREADY_EXISTS(5002, "You have already used this custom identifier.");

    private final int code;
    private final String message;

    private FrejaEidErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
