package com.verisec.frejaeid.client.enums;

/**
 * Describes the type of user information supplied to identify the end user.
 * UserInfoTypes that are currently supported for initiating transaction:
 * <br> - {@link #EMAIL}
 * <br> - {@link #SSN}
 * <br> - {@link #PHONE}
 * <br> - {@link #INFERRED}
 * <br> - {@link #ORG_ID}
 * <br> - {@link #UPI}
 */
public enum UserInfoType {

    /**
     * UserInfoType email for initiating transaction with email of user.
     */
    EMAIL,
    /**
     * UserInfoType ssn for initiating transaction with personal number of user.
     */
    SSN,
    /**
     * UserInfoType phone for initiating transaction with phone number of user.
     */
    PHONE,
    /**
     * UserInfoType inferred for initiating only authentication transaction with
     * QR code.
     */
    INFERRED,
    /**
     * UserInfoType org_id for initiating transaction with organisation id of
     * user.
     */
    ORG_ID,
    /**
     * UserInfoType upi for initiating transaction with unique personal identifier of
     * user.
     */
    UPI;
}
