package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.AddressSourceType;
import com.verisec.frejaeid.client.enums.AddressType;
import com.verisec.frejaeid.client.enums.Country;
import java.util.Objects;

/**
 * AddressInfo contains information about user's address. It can be requested as
 * attribute.
 * 
 */
public class AddressInfo {

    private final Country country;
    private final String city;
    private final String postCode;
    private final String address1;
    private final String address2;
    private final String address3;
    private final String validFrom;
    private final AddressType type;
    private final AddressSourceType sourceType;
    
    @JsonCreator
    public AddressInfo(@JsonProperty("country") Country country, @JsonProperty("city") String city, @JsonProperty("postCode") String postCode,
            @JsonProperty("address1") String address1, @JsonProperty("address2") String address2, @JsonProperty("address3") String address3,
            @JsonProperty("validFrom") String validFrom, @JsonProperty("type") AddressType type, @JsonProperty("sourceType") AddressSourceType sourceType) {
        this.country = country;
        this.city = city;
        this.postCode = postCode;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.validFrom = validFrom;
        this.type = type;
        this.sourceType = sourceType;
    }

    public Country getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public AddressType getType() {
        return type;
    }

    public AddressSourceType getSourceType() {
        return sourceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, postCode, address1, address2, address3, validFrom, type, sourceType);
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
        final AddressInfo other = (AddressInfo) obj;
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.postCode, other.postCode)) {
            return false;
        }
        if (!Objects.equals(this.address1, other.address1)) {
            return false;
        }
        if (!Objects.equals(this.address2, other.address2)) {
            return false;
        }
        if (!Objects.equals(this.address3, other.address3)) {
            return false;
        }
        if (!Objects.equals(this.validFrom, other.validFrom)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.sourceType != other.sourceType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AddressInfo{" + "country=" + country + ", city=" + city + ", postCode=" + postCode + ", address1=" + address1 + ", address2=" + address2 + ", address3=" + address3 + ", validFrom=" + validFrom + ", type=" + type + ", sourceType=" + sourceType + '}';
    }

}
