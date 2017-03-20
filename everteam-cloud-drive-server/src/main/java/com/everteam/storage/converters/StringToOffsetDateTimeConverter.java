package com.everteam.storage.converters;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String source) {
        return OffsetDateTime.parse(source,DateTimeFormatter.ISO_DATE_TIME);
    }

}
