package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * PhoneNumberInfo contains information about user's phone number. It can be requested as
 * attribute to return.
 *
 */
public class PhoneNumberInfo {

    private final String phoneNumber;

    @JsonCreator
    public PhoneNumberInfo(@JsonProperty("phoneNumber") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhoneNumberInfo other = (PhoneNumberInfo) obj;
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PhoneNumberInfo{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}