package com.everteam.storage.jackson;

import java.io.IOException;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.services.ESFileId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ESRepositorySerializer extends JsonSerializer<ESRepository> {
    private final JsonSerializer<Object> defaultSerializer;

    public ESRepositorySerializer(JsonSerializer<Object> defaultSerializer) {
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(ESRepository repository, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            String uri =  new ESFileId(repository.getId(), null).toUri();
            repository.setId(Encryptor.encrypt(uri));
           
            repository.setId(Encryptor.encrypt(uri));
            defaultSerializer.serialize(repository, gen, provider);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
