package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author vemijel
 */
public class AttributeToReturnInfo {

    private final String attribute;

    @JsonCreator
    public AttributeToReturnInfo(@JsonProperty("attribute") String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute);
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
        final AttributeToReturnInfo other = (AttributeToReturnInfo) obj;
        if (!Objects.equals(this.attribute, other.attribute)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AttributeToReturn{" + "attribute=" + attribute + '}';
    }

}
