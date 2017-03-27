package com.everteam.storage.jackson;

import java.io.IOException;
import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.common.model.ESRepository;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        
        //simpleModule.addSerializer(ESFileId.class, new FileIdSerializer());
        //simpleModule.addDeserializer(ESFileId.class, new FileIdDeserializer());
        simpleModule.setSerializerModifier(new StorageSerializerModifier());
        simpleModule.setDeserializerModifier(new StorageDeserializerModifier());
        objectMapper.registerModule(simpleModule);
        
        return objectMapper;
    }
    
    public static class ESRepositorySerializer extends JsonSerializer<ESRepository> {
        private final JsonSerializer<Object> defaultSerializer;

        public ESRepositorySerializer(JsonSerializer<Object> defaultSerializer) {
            this.defaultSerializer = defaultSerializer;
        }

        @Override
        public void serialize(ESRepository repository, JsonGenerator gen, SerializerProvider provider) throws IOException {
            try {
                String uri = new URI(repository.getName(), "", "/" , null).toString();
                repository.setId(Encryptor.encrypt(uri));
                defaultSerializer.serialize(repository, gen, provider);
            } catch (Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
    }
    
    
    public static class ESFileSerializer extends JsonSerializer<ESFile> {
        private final JsonSerializer<Object> defaultSerializer;

        public ESFileSerializer(JsonSerializer<Object> defaultSerializer) {
            this.defaultSerializer = defaultSerializer;
        }

        @Override
        public void serialize(ESFile file, JsonGenerator gen, SerializerProvider provider) throws IOException {
            try {
                String uri = new URI(file.getRepositoryId(), "", "/" +  file.getId(), null).toString();
                file.setId(Encryptor.encrypt(uri));
                
                for (ESParent parent : file.getParents()) {
                    String parentId = new URI(file.getRepositoryId(), "", "/" + parent.getId() , null).toString();
                    parent.setId(Encryptor.encrypt(parentId));
                }
                String repositoryuri = new URI(file.getRepositoryId(), "", "/" , null).toString();
                file.setRepositoryId(Encryptor.encrypt(repositoryuri));

                defaultSerializer.serialize(file, gen, provider);
            } catch (Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
    }
    
    
    public static class ESRepositoryDeserializer extends JsonDeserializer<ESRepository> {
        private final JsonDeserializer<Object> defaultDeserializer;

        public ESRepositoryDeserializer(JsonDeserializer<Object> defaultDeserializer) {
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public ESRepository deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            try {
                ESRepository repository = (ESRepository)  defaultDeserializer.deserialize(p, ctxt);
                repository.setId(Encryptor.decrypt(repository.getId()));
                return repository;
            } catch (Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
    }
    
    
    public static class ESFileDeserializer extends JsonDeserializer<ESFile> {
        private final JsonDeserializer<Object> defaultDeserializer;

        public ESFileDeserializer(JsonDeserializer<Object> defaultDeserializer) {
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public ESFile deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            try {
                ESFile file = (ESFile)  defaultDeserializer.deserialize(p, ctxt);
                file.setId(Encryptor.decrypt(file.getId()));
                
                return file;
            } catch (Exception e) {
                throw new IOException(e.getMessage(), e);
            }
        }
    }
    
    
    
    public static class StorageSerializerModifier extends BeanSerializerModifier {
        @SuppressWarnings("unchecked")
        @Override
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
            if (beanDesc.getBeanClass() == ESRepository.class) {
                return new ESRepositorySerializer((JsonSerializer<Object>) serializer);
            }
            else if (beanDesc.getBeanClass() == ESFile.class) {
                return new ESFileSerializer((JsonSerializer<Object>) serializer);
            }
            return super.modifySerializer(config, beanDesc, serializer);
        }
    }
    
    public static class StorageDeserializerModifier extends BeanDeserializerModifier {
        @SuppressWarnings("unchecked")
        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                JsonDeserializer<?> deserializer) {
            if (beanDesc.getBeanClass() == ESRepository.class) {
                return new ESRepositoryDeserializer((JsonDeserializer<Object>) deserializer);
            }
            else if (beanDesc.getBeanClass() == ESFile.class) {
                return new ESFileDeserializer((JsonDeserializer<Object>) deserializer);
            }
            return super.modifyDeserializer(config, beanDesc, deserializer);
        }
        
        
        
       
    }
    
}
