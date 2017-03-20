package com.everteam.storage.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.everteam.storage.common.model.ESFileId;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        
        simpleModule.addSerializer(ESFileId.class, new FileIdSerializer());
        simpleModule.addDeserializer(ESFileId.class, new FileIdDeserializer());
        objectMapper.registerModule(simpleModule);
        
        return objectMapper;
    }
    
    
    
}
