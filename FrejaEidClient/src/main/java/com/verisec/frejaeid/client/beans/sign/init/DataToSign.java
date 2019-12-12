package com.verisec.frejaeid.client.beans.sign.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * DataToSign can contain text data and binary data. Text data will be shown to
 * user and has to be set for every sign transaction and binary data is
 * optional.
 *
 */
public class DataToSign {

    private final String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String binaryData;

    public static DataToSign create(String text) {
        return new DataToSign(text, (byte[]) null);
    }

    public static DataToSign create(String text, byte[] binaryData) {
        return new DataToSign(text, binaryData);
    }

    private DataToSign(String text, byte[] binaryData) {
        this.text = text == null ? null : Base64.encodeBase64String((text.getBytes(StandardCharsets.UTF_8)));
        this.binaryData = binaryData == null ? null : Base64.encodeBase64String(binaryData);
    }

    @JsonCreator
    private DataToSign(@JsonProperty(value = "text") String text, @JsonProperty(value = "binaryData") String binaryData) {
        this.text = text;
        this.binaryData = binaryData;
    }

    public String getText() {
        return text;
    }

    public String getBinaryData() {
        return binaryData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, binaryData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataToSign other = (DataToSign) obj;
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return Objects.equals(this.binaryData, other.binaryData);
    }

    @Override
    public String toString() {
        return "DataToSign{" + "text=" + text + ", binaryData=" + binaryData + '}';
    }

}
