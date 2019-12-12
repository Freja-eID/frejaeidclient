package com.verisec.frejaeid.client.enums;

public enum Country {
    SWEDEN("SE"),
    FINLAND("FI"),
    DENMARK("DK"),
    NORWAY("NO");

    private final String countryCode;

    private Country(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public static Country getByCountryCode(String countryCode) {
        for (Country country : values()) {
            if (country.getCountryCode().equals(countryCode)) {
                return country;
            }
        }
        return null;
    }
}
