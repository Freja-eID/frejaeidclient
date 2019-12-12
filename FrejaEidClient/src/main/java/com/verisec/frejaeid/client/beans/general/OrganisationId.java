package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Contains information regarding organisation id.
 *
 */
public class OrganisationId {

    private final String title;
    private final String identifierName;
    private final String identifier;

    /**
     * Creates instance of {@linkplain OrganisationId}.
     *
     * @param title will be shown to user on organisation id card (for example
     * "Developers"). Maximum length is 20 characters. It cannot be {@code null}
     * or empty.
     * @param identifierName name of identifier that will be set to user (for
     * example "employee number"). Maximum length is 25 characters. It cannot be
     * {@code null} or empty.
     * @param identifier will be set for user. Can be used for initiating
     * transaction. Maximum length is 25 characters. It cannot be {@code null}
     * or empty.
     * @return informations for organisation id
     */
    public static OrganisationId create(String title, String identifierName, String identifier) {
        return new OrganisationId(title, identifierName, identifier);
    }

    @JsonCreator
    private OrganisationId(@JsonProperty("title") String title, @JsonProperty("identifierName") String identifierName, @JsonProperty("identifier") String identifier) {
        this.title = title;
        this.identifierName = identifierName;
        this.identifier = identifier;
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

    @Override
    public int hashCode() {
        return Objects.hash(title, identifierName, identifier);
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
        final OrganisationId other = (OrganisationId) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.identifierName, other.identifierName)) {
            return false;
        }
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrganisationId{" + "title=" + title + ", identifierName=" + identifierName + ", identifier=" + identifier + '}';
    }

}
