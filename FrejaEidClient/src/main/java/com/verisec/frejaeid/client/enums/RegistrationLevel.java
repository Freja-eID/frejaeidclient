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

    private final String level;

    private RegistrationLevel(String level) {
        this.level = level;
    }

    /**
     * Returns level of the RegistrationLevel constant.
     *
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Returns the RegistrationLevel constant of this type with the specified
     * level.
     *
     * @param level The level must match exactly an identifier used to declare
     *              an RegistrationLevel constant in this type. (Extraneous whitespace
     *              characters are not permitted.)
     * @return The RegistrationLevel constant with the specified name and
     * {@code null} if there is no constant with the specified name.
     */
    public static RegistrationLevel getByLevel(String level) {
        for (RegistrationLevel registrationLevel : values()) {
            if (registrationLevel.level.equals(level)) {
                return registrationLevel;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RegistrationLevel{" + "level=" + level + '}';
    }
}
