package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.Country;
import java.util.Objects;

/**
 * SsnUserInfo contains country and personal number. It can be used to start
 * transaction or requested as attribute.
 *
 */
public class SsnUserInfo {

    private final Country country;
    private final String ssn;

    @JsonCreator
    public SsnUserInfo(@JsonProperty("country") Country country, @JsonProperty("ssn") String ssn) {
        this.country = country;
        this.ssn = ssn;
    }

    public Country getCountry() {
        return country;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, ssn);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SsnUserInfo other = (SsnUserInfo) obj;
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        return Objects.equals(this.ssn, other.ssn);
    }

    @Override
    public String toString() {
        return "SsnUserInfo{" + "country=" + country + ", ssn=" + ssn + '}';
    }

}
