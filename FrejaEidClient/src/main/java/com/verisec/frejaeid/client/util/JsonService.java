package com.verisec.frejaeid.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class JsonService implements Serializable {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonService() {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }

    public <T> String serializeToJson(T jsonSerializable) throws FrejaEidClientInternalException {
        return serialize(jsonSerializable);
    }

    public <V> V deserializeFromJson(byte[] bodyBytes, Class<V> responseType) throws FrejaEidClientInternalException {
        return deserialize(bodyBytes, responseType, mapper);
    }

    private <T> String serialize(T jsonSerializable) throws FrejaEidClientInternalException {
        try {
            return mapper.writeValueAsString(jsonSerializable);
        } catch (JsonProcessingException ex) {
            throw new FrejaEidClientInternalException(
                    String.format("Error while serializing %s. ", jsonSerializable), ex);
        }
    }

    private <T> T deserialize(byte[] value, Class<? extends T> type, ObjectMapper mapper)
            throws FrejaEidClientInternalException {
        try {
            return mapper.readValue(value, type);
        } catch (IOException ex) {
            throw new FrejaEidClientInternalException(
                    String.format("Failed to deserialize value %s into object of class %s",
                                  new String(value, StandardCharsets.UTF_8), type.getName()), ex);
        }
    }

}
