package com.verisec.frejaeid.client.enums;

/**
 * @author veiszec
 **/
/**
 * Current registration level of a user
 * <br> - {@linkplain #BASIC}
 * <br> - {@linkplain #EXTENDED}
 * <br> - {@linkplain #PLUS}
 */
public enum RegistrationLevel {
    BASIC("BASIC"),
    EXTENDED("EXTENDED"),
    PLUS("PLUS");

    private final String state;

    RegistrationLevel(String state) {
        this.state = state;
    }

    /**
     * Returns state of the RegistrationLevel constant.
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the RegistrationLevel constant of this type with the specified
     * state.
     *
     * @param state The state must match exactly an identifier used to declare
     *              an RegistrationLevel constant in this type. (Extraneous whitespace
     *              characters are not permitted.)
     * @return The RegistrationLevel constant with the specified name and
     * {@code null} if there is no constant with the specified name.
     */
    public static RegistrationLevel getByState(String state) {
        for (RegistrationLevel regState : values()) {
            if (regState.state.equals(state)) {
                return regState;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RegistrationLevel{" + "state=" + state + '}';
    }
}
