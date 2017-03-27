package com.everteam.storage.converters;

import org.springframework.core.convert.converter.Converter;

import com.everteam.storage.jackson.Encryptor;
import com.everteam.storage.utils.ESFileId;

public class FileIdConverter implements Converter<String, ESFileId> {

    @Override
    public ESFileId convert(String source) {
        try {
            return new ESFileId(Encryptor.decrypt(source));
        } catch (Throwable e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
