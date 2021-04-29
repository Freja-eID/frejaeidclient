package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.RegistrationLevel;

import java.util.List;
import java.util.Objects;

/**
 * When retrieving results, additional information about the user can be
 * returned based on the type of attributes required through request. If
 * attribute is not required it will be {@code null}.
 */
public class RequestedAttributes {

    private final BasicUserInfo basicUserInfo;
    private final String customIdentifier;
    private final SsnUserInfo ssn;
    private final String integratorSpecificUserId;
    private final String dateOfBirth;
    private final String relyingPartyUserId;
    private final String emailAddress;
    private final String organisationIdIdentifier;
    private final List<AddressInfo> addresses;
    private final List<Email> allEmailAddresses;
    private final List<PhoneNumberInfo> allPhoneNumbers;
    private final RegistrationLevel registrationLevel;
    private final Integer age;
    private final GreenCertificate greenCertificate;

    @JsonCreator
    public RequestedAttributes(@JsonProperty(value = "basicUserInfo") BasicUserInfo basicUserInfo,
                               @JsonProperty(value = "customIdentifier") String customIdentifier,
                               @JsonProperty(value = "ssn") SsnUserInfo ssn,
                               @JsonProperty(value = "integratorSpecificUserId") String integratorSpecificUserId,
                               @JsonProperty(value = "dateOfBirth") String dateOfBirth,
                               @JsonProperty(value = "relyingPartyUserId") String relyingPartyUserId,
                               @JsonProperty(value = "emailAddress") String emailAddress,
                               @JsonProperty(value = "organisationIdIdentifier") String organisationIdIdentifier,
                               @JsonProperty(value = "addresses") List<AddressInfo> addresses,
                               @JsonProperty(value = "allEmailAddresses") List<Email> allEmailAddresses,
                               @JsonProperty(value = "allPhoneNumbers") List<PhoneNumberInfo> allPhoneNumbers,
                               @JsonProperty(value = "registrationLevel") RegistrationLevel registrationLevel,
                               @JsonProperty(value = "age") Integer age,
                               @JsonProperty(value = "greenCertificate") GreenCertificate greenCertificate) {
        this.basicUserInfo = basicUserInfo;
        this.customIdentifier = customIdentifier;
        this.ssn = ssn;
        this.integratorSpecificUserId = integratorSpecificUserId;
        this.dateOfBirth = dateOfBirth;
        this.relyingPartyUserId = relyingPartyUserId;
        this.emailAddress = emailAddress;
        this.organisationIdIdentifier = organisationIdIdentifier;
        this.addresses = addresses;
        this.allEmailAddresses = allEmailAddresses;
        this.allPhoneNumbers = allPhoneNumbers;
        this.registrationLevel = registrationLevel;
        this.age = age;
        this.greenCertificate = greenCertificate;
    }

    public BasicUserInfo getBasicUserInfo() {
        return basicUserInfo;
    }

    public String getCustomIdentifier() {
        return customIdentifier;
    }

    public SsnUserInfo getSsn() {
        return ssn;
    }

    public String getIntegratorSpecificUserId() {
        return integratorSpecificUserId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRelyingPartyUserId() {
        return relyingPartyUserId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getOrganisationIdIdentifier() {
        return organisationIdIdentifier;
    }

    public List<AddressInfo> getAddresses() {
        return addresses;
    }

    public List<Email> getAllEmailAddresses() {
        return allEmailAddresses;
    }

    public List<PhoneNumberInfo> getAllPhoneNumbers() {
        return allPhoneNumbers;
    }

    public RegistrationLevel getRegistrationLevel() {
        return registrationLevel;
    }

    public Integer getAge() {
        return age;
    }

    public GreenCertificate getGreenCertificate() {
        return greenCertificate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(basicUserInfo, customIdentifier, ssn, integratorSpecificUserId,
                            dateOfBirth, relyingPartyUserId, emailAddress, organisationIdIdentifier,
                            addresses, allEmailAddresses, allPhoneNumbers, registrationLevel, greenCertificate);
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
        final RequestedAttributes other = (RequestedAttributes) obj;
        if (!Objects.equals(this.customIdentifier, other.customIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.integratorSpecificUserId, other.integratorSpecificUserId)) {
            return false;
        }
        if (!Objects.equals(this.dateOfBirth, other.dateOfBirth)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyUserId, other.relyingPartyUserId)) {
            return false;
        }
        if (!Objects.equals(this.emailAddress, other.emailAddress)) {
            return false;
        }
        if (!Objects.equals(this.organisationIdIdentifier, other.organisationIdIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.basicUserInfo, other.basicUserInfo)) {
            return false;
        }
        if (!Objects.equals(this.ssn, other.ssn)) {
            return false;
        }
        if (!Objects.equals(this.addresses, other.addresses)) {
            return false;
        }
        if (!Objects.equals(this.allEmailAddresses, other.allEmailAddresses)) {
            return false;
        }
        if (!Objects.equals(this.allPhoneNumbers, other.allPhoneNumbers)) {
            return false;
        }
        if (!Objects.equals(this.registrationLevel, other.registrationLevel)) {
            return false;
        }
        if (!Objects.equals(this.age, other.age)) {
            return false;
        }
        if (!Objects.equals(this.greenCertificate, other.greenCertificate)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "RequestedAttributes{" +
                "basicUserInfo=" + basicUserInfo +
                ", customIdentifier='" + customIdentifier + '\'' +
                ", ssn=" + ssn +
                ", integratorSpecificUserId='" + integratorSpecificUserId + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", relyingPartyUserId='" + relyingPartyUserId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", organisationIdIdentifier='" + organisationIdIdentifier + '\'' +
                ", addresses=" + addresses +
                ", allEmailAddresses=" + allEmailAddresses +
                ", allPhoneNumbers=" + allPhoneNumbers +
                ", registrationLevel=" + registrationLevel +
                ", age=" + age +
                ", greenCertificate=" + greenCertificate +
                '}';
    }
}
