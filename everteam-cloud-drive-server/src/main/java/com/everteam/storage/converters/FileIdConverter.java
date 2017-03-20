package com.everteam.storage.converters;

import java.net.URI;

import org.springframework.core.convert.converter.Converter;

import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.jackson.Encryptor;

public class FileIdConverter implements Converter<String, ESFileId> {

    @Override
    public ESFileId convert(String source) {
        try {
            URI uri = new URI(Encryptor.decrypt(source));
            return new ESFileId().repositoryName(uri.getScheme()).relativeId(uri.getPath().substring(1));
        } catch (Throwable e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
