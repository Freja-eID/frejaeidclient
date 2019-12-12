package com.verisec.frejaeid.client.enums;

/**
 * Transaction can be in following statuses:
 * <br> - {@link #STARTED}
 * <br> - {@link #DELIVERED_TO_MOBILE}
 * <br> - {@link #CANCELED} - final state
 * <br> - {@link #RP_CANCELED} - final state
 * <br> - {@link #EXPIRED} - final state
 * <br> - {@link #APPROVED} - final state
 * <br> - {@link #REJECTED} - final state
 *
 */
public enum TransactionStatus {

    /**
     * Transaction status when request is received on server side.
     */
    STARTED,
    /**
     * Transaction status when transaction is delivered to user's mobile.
     */
    DELIVERED_TO_MOBILE,
    /**
     * Transaction status when transaction is declined by user.
     */
    CANCELED,
    /**
     * Transaction status when transaction is canceled by relying party.
     */
    RP_CANCELED,
    /**
     * Transaction status for an expired transaction in the case when the user
     * has not approved or declined the transaction in the set time frame.
     */
    EXPIRED,
    /**
     * Transaction status when transaction is approved by user.
     */
    APPROVED,
    /**
     * Transaction status when transaction is rejected due to security reasons.
     */
    REJECTED;

}
