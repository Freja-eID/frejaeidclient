package com.verisec.frejaeid.client.enums;

/**
 * When a transaction is started on behalf of the organisation,
 * {@linkplain #ORGANISATIONAL} context is used, when a transaction is started
 * on personal behalf, {@linkplain #PERSONAL} context is used.
 *
 */
public enum TransactionContext {
    PERSONAL("PERSONAL"),
    ORGANISATIONAL("ORGANISATIONAL");

    private final String context;

    private TransactionContext(String transactionContext) {
        this.context = transactionContext;
    }

    /**
     * Returns context of the TransactionContext constant
     *
     * @return context
     */
    public String getContext() {
        return context;
    }

    /**
     * Returns the TransactionContext constant of this type with the specified
     * context.
     *
     * @param context The context must match exactly an identifier used to
     * declare a TransactionContext constant in this type. (Extraneous
     * whitespace characters are not permitted.)
     * @return The TransactionContext constant with the specified context and
     * {@code null} if there is no constant with the specified context.
     */
    public static TransactionContext getByContext(String context) {
        for (TransactionContext transactionContext : values()) {
            if (transactionContext.context.equals(context)) {
                return transactionContext;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TransactionContext{" + "context=" + context + '}';
    }

}
