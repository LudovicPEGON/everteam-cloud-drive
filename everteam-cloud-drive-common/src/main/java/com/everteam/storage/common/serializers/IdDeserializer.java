package com.everteam.storage.common.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class IdDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String id = p.readValueAs(String.class);
        if (id != null) {
            try {
                id = Encryptor.decrypt(id);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }   
        return id;
    }

}
