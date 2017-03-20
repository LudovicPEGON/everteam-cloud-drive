package com.everteam.storage.jackson;

import java.io.IOException;

import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.converters.FileIdConverter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FileIdDeserializer extends JsonDeserializer<ESFileId> {

    @Override
    public ESFileId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            String id = p.getValueAsString();
            return new FileIdConverter().convert(id);

        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

}
