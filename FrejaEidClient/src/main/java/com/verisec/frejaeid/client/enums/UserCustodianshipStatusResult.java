package com.verisec.frejaeid.client.enums;

/**
 *
 * @author veiggnu
 */
public enum UserCustodianshipStatusResult {
    /**
     * Custodianship status when the user has a custodian.
     */
    USER_HAS_CUSTODIAN("USER_HAS_CUSTODIAN"),
    /**
     * Custodianship status when it's unknown if the user has a custodian.
     */
    UNKNOWN("UNKNOWN");

    private final String status;

    UserCustodianshipStatusResult(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static UserCustodianshipStatusResult getByStatus(String status) {
        for (UserCustodianshipStatusResult userCustodianshipStatusResult : values()) {
            if (userCustodianshipStatusResult.status.equals(status)) {
                return userCustodianshipStatusResult;
            }
        }
        return null;
    }

}