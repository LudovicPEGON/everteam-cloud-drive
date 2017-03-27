package com.everteam.storage.jackson;

import java.io.IOException;
import java.net.URISyntaxException;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.services.ESFileId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ESFileSerializer extends JsonSerializer<ESFile> {
    private final JsonSerializer<Object> defaultSerializer;

    public ESFileSerializer(JsonSerializer<Object> defaultSerializer) {
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(ESFile file, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            file.setId(serialize(file.getRepositoryId(), file.getId()));
            
            for (ESParent parent : file.getParents()) {
                parent.setId(serialize(file.getRepositoryId(), parent.getId()));
            }
            file.setRepositoryId(serialize(file.getRepositoryId(), null));
            defaultSerializer.serialize(file, gen, provider);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
    
    public static String serialize(String repositoryId, String fileId) throws URISyntaxException, IOException {
        String fileUri =  new ESFileId(repositoryId, fileId).toUri();
        return Encryptor.encrypt(fileUri);
    }
}