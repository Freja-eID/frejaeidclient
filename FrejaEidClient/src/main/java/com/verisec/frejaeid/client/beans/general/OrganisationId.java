package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.enums.DisplayType;

import java.util.List;
import java.util.Objects;

/**
 * Contains information regarding organisation id.
 */
public class OrganisationId {

    private final String title;
    private final String identifierName;
    private final String identifier;
    private final List<DisplayType> identifierDisplayTypes;
    private final List<OrganisationIdAttribute> additionalAttributes;

    /**
     * Creates instance of {@linkplain OrganisationId}.
     *
     * @param title                  will be shown to user on organisation id card (for example
     *                               "Developers"). Maximum length is 22 characters. It cannot be {@code null}
     *                               or empty.
     * @param identifierName         name of identifier that will be set to user (for
     *                               example "employee number"). Maximum length is 30 characters. It cannot be
     *                               {@code null} or empty.
     * @param identifier             will be set for user. Can be used for initiating
     *                               transaction. Maximum length is 128 characters. It cannot be {@code null}
     *                               or empty.
     * @param identifierDisplayTypes determines in what ways the identifier is displayed to the end user. It can be
     *                               {@code null} or empty. TEXT is used by default.
     * @param additionalAttributes   additional attributes related to the identifier. It can be {@code null} or empty.
     * @return information for organisation id
     */
    public static OrganisationId create(String title, String identifierName, String identifier,
                                        List<DisplayType> identifierDisplayTypes,
                                        List<OrganisationIdAttribute> additionalAttributes) {
        return new OrganisationId(title, identifierName, identifier, identifierDisplayTypes, additionalAttributes);
    }

    public static OrganisationId create(String title, String identifierName, String identifier) {
        return new OrganisationId(title, identifierName, identifier, null, null);
    }

    @JsonCreator
    private OrganisationId(@JsonProperty("title") String title,
                           @JsonProperty("identifierName") String identifierName,
                           @JsonProperty("identifier") String identifier,
                           @JsonProperty("identifierDisplayTypes") List<DisplayType> identifierDisplayTypes,
                           @JsonProperty("additionalAttributes") List<OrganisationIdAttribute> additionalAttributes) {
        this.title = title;
        this.identifierName = identifierName;
        this.identifier = identifier;
        this.identifierDisplayTypes = identifierDisplayTypes;
        this.additionalAttributes = additionalAttributes;
    }

    public String getTitle() {
        return title;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<DisplayType> getIdentifierDisplayTypes() {
        return identifierDisplayTypes;
    }

    public List<OrganisationIdAttribute> getAdditionalAttributes() {
        return additionalAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationId that = (OrganisationId) o;
        return Objects.equals(title, that.title)
                && Objects.equals(identifierName, that.identifierName)
                && Objects.equals(identifier, that.identifier)
                && Objects.equals(identifierDisplayTypes, that.identifierDisplayTypes)
                && Objects.equals(additionalAttributes, that.additionalAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, identifierName, identifier, identifierDisplayTypes, additionalAttributes);
    }

    @Override
    public String toString() {
        return "OrganisationId{" +
                "title='" + title + '\'' +
                ", identifierName='" + identifierName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierDisplayTypes=" + identifierDisplayTypes +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }
}
