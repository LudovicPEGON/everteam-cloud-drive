package com.everteam.storage.jackson;

import java.io.IOException;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.utils.ESFileId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ESRepositoryDeserializer extends JsonDeserializer<ESRepository> {
    private final JsonDeserializer<Object> defaultDeserializer;

    public ESRepositoryDeserializer(JsonDeserializer<Object> defaultDeserializer) {
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public ESRepository deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        try {
            ESRepository repository = (ESRepository)  defaultDeserializer.deserialize(p, ctxt);
            ESFileId repositoryFid = new ESFileId(Encryptor.decrypt(repository.getId()));
            repository.setId(Encryptor.decrypt(repositoryFid.getRepositoryId()));
            return repository;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
