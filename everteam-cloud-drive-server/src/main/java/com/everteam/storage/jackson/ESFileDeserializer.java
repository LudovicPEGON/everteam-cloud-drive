package com.everteam.storage.jackson;

import java.io.IOException;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.utils.ESFileId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ESFileDeserializer extends JsonDeserializer<ESFile> {
    private final JsonDeserializer<Object> defaultDeserializer;

    public ESFileDeserializer(JsonDeserializer<Object> defaultDeserializer) {
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public ESFile deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        try {
            ESFile file = (ESFile)  defaultDeserializer.deserialize(p, ctxt);
            ESFileId fileFid = new ESFileId(Encryptor.decrypt(file.getId()));
            
            file.setId(fileFid.getPath());
            
            
            for (ESParent parent : file.getParents()) {
                ESFileId parentFid = new ESFileId(Encryptor.decrypt(parent.getId()));
                parent.setId(parentFid.getPath());
            }
            ESFileId repositoryFid =  new ESFileId(file.getRepositoryId());
            file.setRepositoryId(repositoryFid.getRepositoryId());
            
            return file;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
