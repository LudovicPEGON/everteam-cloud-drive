package com.everteam.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonIdMapper {
    @Primary
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        // SimpleModule simpleModule = new SimpleModule();
        // simpleModule.addSerializer(ESRepositoryId.class, new
        // JsonSerializer<ESRepositoryId>() {
        // @Override
        // public void serialize(ESRepositoryId repositoryId, JsonGenerator
        // jsonGenerator,
        // SerializerProvider serializerProvider) throws IOException,
        // JsonProcessingException {
        // try {
        // jsonGenerator.writeString(Encryptor.encrypt(repositoryId.getRepositoryId()));
        // } catch (Exception e) {
        // throw new IOException(e);
        // }
        // }
        // });
        // simpleModule.addDeserializer(ESRepositoryId.class, new
        // JsonDeserializer<ESRepositoryId>() {
        //
        // @Override
        // public ESRepositoryId deserialize(JsonParser p,
        // DeserializationContext ctxt)
        // throws IOException, JsonProcessingException {
        // String id = p.getValueAsString();
        // try {
        // return new ESRepositoryId().repositoryId(Encryptor.decrypt(id));
        // } catch (Exception e) {
        // throw new IOException(e);
        // }
        // }
        //
        // });

        // simpleModule.addSerializer(ESFileId.class, new
        // JsonSerializer<ESFileId>() {
        // @Override
        // public void serialize(ESFileId fileId, JsonGenerator jsonGenerator,
        // SerializerProvider serializerProvider) throws IOException,
        // JsonProcessingException {
        // try {
        // String sfileId = fileId.getRepositoryId() + ":/" +
        // fileId.getRepositoryFileId();
        // jsonGenerator.writeString(Encryptor.encrypt(sfileId));
        // } catch (Exception e) {
        // throw new IOException(e);
        // }
        // }
        // });
        // simpleModule.addDeserializer(ESFileId.class, new
        // JsonDeserializer<ESFileId>() {
        //
        // @Override
        // public ESFileId deserialize(JsonParser p, DeserializationContext
        // ctxt)
        // throws IOException, JsonProcessingException {
        // String id = p.getValueAsString();
        // try {
        // return new ESRepositoryId().repositoryId(Encryptor.decrypt(id));
        // } catch (Exception e) {
        // throw new IOException(e);
        // }
        // }
        //
        // });

        // objectMapper.registerModule(simpleModule);

        return objectMapper;
    }

}
