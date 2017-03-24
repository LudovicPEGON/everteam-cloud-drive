package com.everteam.storage.jackson;

import java.io.IOException;
import java.net.URI;

import com.everteam.storage.common.model.ESFileId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FileIdSerializer extends JsonSerializer<ESFileId> {

    @Override
    public void serialize(ESFileId fileId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(encrypt(fileId));
    }

    public static String encrypt(ESFileId fileId) throws IOException {
        try { 
            String path =  fileId.getPath();
            if (path== null) {
                path = "";
            }
            String sESFileId = new URI(fileId.getRepositoryName(), "", "/" + path, null).toString();
            return Encryptor.encrypt(sESFileId);
        }
        catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

}
