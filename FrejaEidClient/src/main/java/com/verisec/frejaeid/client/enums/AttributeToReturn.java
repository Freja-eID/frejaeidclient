package com.verisec.frejaeid.client.enums;

import com.verisec.frejaeid.client.beans.general.BasicUserInfo;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.general.AddressInfo;
import com.verisec.frejaeid.client.beans.general.Email;
import com.verisec.frejaeid.client.beans.general.PhoneNumberInfo;
import com.verisec.frejaeid.client.beans.general.DocumentInfo;
import com.verisec.frejaeid.client.beans.covidcertificate.CovidCertificates;

import java.util.HashMap;
import java.util.Map;

/**
 * Additional information about the user to be returned. Attributes that can be
 * requested:
 * <br> - {@link #BASIC_USER_INFO}
 * <br> - {@link #CUSTOM_IDENTIFIER}
 * <br> - {@link #INTEGRATOR_SPECIFIC_USER_ID}
 * <br> - {@link #SSN}
 * <br> - {@link #DATE_OF_BIRTH}
 * <br> - {@link #RELYING_PARTY_USER_ID}
 * <br> - {@link #EMAIL_ADDRESS}
 * <br> - {@link #ORGANISATION_ID_IDENTIFIER}
 * <br> - {@link #ADDRESSES}
 * <br> - {@link #ALL_EMAIL_ADDRESSES}
 * <br> - {@link #ALL_PHONE_NUMBERS}
 * <br> - {@link #REGISTRATION_LEVEL}
 * <br> - {@link #AGE}
 * <br> - {@link #PHOTO}
 * <br> - {@link #DOCUMENT}
 * <br> - {@link #DOCUMENT_PHOTO}
 * <br> - {@link #COVID_CERTIFICATES}
 * <br> - {@link #DOCUMENT_INFO_WITH_PDF}
 * <br> - {@link #CHILDREN_DOCUMENT_INFO_WITH_PDF}
 */
public enum AttributeToReturn {

    /**
     * If BASIC_USER_INFO is requested, {@linkplain BasicUserInfo} which
     * contains name and surname will be returned and it can be requested only
     * for {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    BASIC_USER_INFO("BASIC_USER_INFO"),
    /**
     * Custom identifier can be requested only if it is set previously.
     */
    CUSTOM_IDENTIFIER("CUSTOM_IDENTIFIER"),
    /**
     * Integrator specific user id can be requested only by integrator relying
     * parties.
     */
    INTEGRATOR_SPECIFIC_USER_ID("INTEGRATOR_SPECIFIC_USER_ID"),
    /**
     * If SSN is requested, {@linkplain SsnUserInfo} which contains personal
     * number and country will be returned and it can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    SSN("SSN"),
    /**
     * Date of birth can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    DATE_OF_BIRTH("DATE_OF_BIRTH"),
    /**
     * Relying party user id is a unique, user-specific value that allows the
     * relying party to identify the same user across multiple sessions.
     */
    RELYING_PARTY_USER_ID("RELYING_PARTY_USER_ID"),
    /**
     * If relying party has one or more email domains connected to it,
     * an email address belonging to one of those domains is returned.
     * Otherwise, the user’s primary email address is returned by default.
     */
    EMAIL_ADDRESS("EMAIL_ADDRESS"),
    /**
     * Organisation id identifier can be requested only if it is set previously.
     */
    ORGANISATION_ID_IDENTIFIER("ORGANISATION_ID_IDENTIFIER"),
    /**
     * Contains Organisation ID identifier and additional information about Organisation ID.
     * Organisation ID can be requested only if it is set previously.
     */
    ORGANISATION_ID("ORGANISATION_ID"),
    /**
     * If ADDRESSES is requested, list of {@linkplain AddressInfo} which
     * contains user's current physical addresses will be returned and it can be
     * requested only for {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    ADDRESSES("ADDRESSES"),
    /**
     * If ALL_EMAIL_ADDRESSES is requested, list of {@linkplain Email} which
     * contains all user's email addresses registered in Freja eID will be
     * returned.
     */
    ALL_EMAIL_ADDRESSES("ALL_EMAIL_ADDRESSES"),
    /**
     * If ALL_PHONE_NUMBERS is requested, list of {@linkplain PhoneNumberInfo} which
     * contains all user's phone numbers registered in Freja eID will be
     * returned.
     */
    ALL_PHONE_NUMBERS("ALL_PHONE_NUMBERS"),
    /**
     * Current registration level of the user.
     * Corresponds to one of the values of {@linkplain RegistrationLevel}.
     */
    REGISTRATION_LEVEL("REGISTRATION_LEVEL"),
    /**
     * User's age returned as an integer.
     * Age can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    AGE("AGE"),
    /**
     * User's photo returned as a Base64-encoded String
     * Photo can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    PHOTO("PHOTO"),
    /**
     * Data about the user's document returned as {@linkplain DocumentInfo}
     * Document can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    DOCUMENT("DOCUMENT"),
    /**
     * ID document photo returned as a Base64-encoded String.
     * Photo can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    DOCUMENT_PHOTO("DOCUMENT_PHOTO"),
    /**
     * Proof that the user has been vaccinated against COVID-19,
     * received a negative test result or recovered from COVID-19,
     * returned as {@linkplain CovidCertificates}.
     * Covid Certificates can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    COVID_CERTIFICATES("COVID_CERTIFICATES"),
    /**
     * Data about the user's document and accompanying PDF file as a Base64-encoded String.
     * Document with PDF can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    DOCUMENT_INFO_WITH_PDF("DOCUMENT_INFO_WITH_PDF"),
    /**
     * Data about the user's children's documents and accompanying PDF files as a Base64-encoded String.
     * Children documents with PDF can be requested only for
     * {@linkplain MinRegistrationLevel#EXTENDED} and
     * {@linkplain MinRegistrationLevel#PLUS}.
     */
    CHILDREN_DOCUMENT_INFO_WITH_PDF("CHILDREN_DOCUMENT_INFO_WITH_PDF"),

    /**
     * Data contains information about the client device network connection.
     */
    NETWORK_INFO("NETWORK_INFO");

    private final String name;

    private static class Holder {

        static Map<String, AttributeToReturn> MAP = new HashMap<>();
    }

    private AttributeToReturn(String name) {
        this.name = name;
        AttributeToReturn.Holder.MAP.put(name, this);
    }

    /**
     * Returns the AttributeToReturn constant of this type with the specified
     * name.
     *
     * @param name The name must match exactly an identifier used to declare an
     *             AttributeToReturn constant in this type. (Extraneous whitespace
     *             characters are not permitted.)
     * @return The AttributeToReturn constant with the specified name and
     * {@code null} if there is no constant with the specified name.
     */
    public static AttributeToReturn findByName(String name) {
        return AttributeToReturn.Holder.MAP.get(name);
    }

    /**
     * Returns name of the AttributeToReturn constant
     *
     * @return name
     */
    public String getName() {
        return name;
    }

}
