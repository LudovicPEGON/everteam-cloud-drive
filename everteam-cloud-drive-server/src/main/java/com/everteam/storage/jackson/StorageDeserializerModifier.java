package com.everteam.storage.jackson;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESRepository;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

public class StorageDeserializerModifier extends BeanDeserializerModifier {
    //@SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
            JsonDeserializer<?> deserializer) {
        if (beanDesc.getBeanClass() == ESRepository.class) {
            //return new ESRepositoryDeserializer((JsonDeserializer<Object>) deserializer);
        } else if (beanDesc.getBeanClass() == ESFile.class) {
//            return new ESFileDeserializer((JsonDeserializer<Object>) deserializer);
        }
        return super.modifyDeserializer(config, beanDesc, deserializer);
    }

}
