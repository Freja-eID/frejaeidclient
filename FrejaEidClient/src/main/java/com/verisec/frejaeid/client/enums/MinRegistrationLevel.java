package com.verisec.frejaeid.client.enums;

/**
 * Minimum required registration level of a user in order to approve/decline
 * transaction:
 * <br> - {@linkplain #BASIC}
 * <br> - {@linkplain #EXTENDED}
 * <br> - {@linkplain #PLUS}
 * <br> - {@linkplain #INFERRED}
 */
public enum MinRegistrationLevel {

    BASIC("BASIC"),
    EXTENDED("EXTENDED"),
    PLUS("PLUS"),
    INFERRED("INFERRED");

    private final String state;

    private MinRegistrationLevel(String state) {
        this.state = state;
    }

    /**
     * Returns state of the MinRegistrationLevel constant.
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the MinRegistrationLevel constant of this type with the specified
     * state.
     *
     * @param state The state must match exactly an identifier used to declare
     *              an MinRegistrationLevel constant in this type. (Extraneous whitespace
     *              characters are not permitted.)
     * @return The MinRegistrationLevel constant with the specified name and
     * {@code null} if there is no constant with the specified name.
     */
    public static MinRegistrationLevel getByState(String state) {
        for (MinRegistrationLevel regState : values()) {
            if (regState.state.equals(state)) {
                return regState;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MinRegistrationLevel{" + "state=" + state + '}';
    }

}
