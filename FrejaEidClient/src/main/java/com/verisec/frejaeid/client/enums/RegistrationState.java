package com.verisec.frejaeid.client.enums;

/**
 * User's registration state in Freja eID:
 * <br> - {@linkplain #EMAIL_CONFIRMED}
 * <br> - {@linkplain #BASIC}
 * <br> - {@linkplain #APPLIED_FOR_EXTENDED}
 * <br> - {@linkplain #EXTENDED}
 * <br> - {@linkplain #VETTING_CONFIRMED}
 * <br> - {@linkplain #PLUS}
 *
 */
public enum RegistrationState {

    EMAIL_CONFIRMED("EMAIL_CONFIRMED"),
    BASIC("BASIC"),
    APPLIED_FOR_EXTENDED("APPLIED_FOR_EXTENDED"),
    EXTENDED("EXTENDED"),
    VETTING_CONFIRMED("VETTING_CONFIRMED"),
    PLUS("PLUS");

    private final String state;

    private RegistrationState(String state) {
        this.state = state;
    }

    /**
     * Returns state of the RegistrationState constant.
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the RegistrationState constant of this type with the specified
     * state.
     *
     * @param state The state must match exactly an identifier used to declare
     * an RegistrationState constant in this type. (Extraneous whitespace
     * characters are not permitted.)
     * @return The RegistrationState constant with the specified name and
     * {@code null} if there is no constant with the specified name.
     */
    public static RegistrationState getByState(String state) {
        for (RegistrationState regState : values()) {
            if (regState.state.equals(state)) {
                return regState;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RegistrationState{" + "state=" + state + '}';
    }

}
