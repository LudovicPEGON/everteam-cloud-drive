package com.everteam.storage.jackson;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESRepository;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class StorageSerializerModifier extends BeanSerializerModifier {
    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
            JsonSerializer<?> serializer) {
        if (beanDesc.getBeanClass() == ESRepository.class) {
            return new ESRepositorySerializer((JsonSerializer<Object>) serializer);
        } else if (beanDesc.getBeanClass() == ESFile.class) {
            return new ESFileSerializer((JsonSerializer<Object>) serializer);
        }
        return super.modifySerializer(config, beanDesc, serializer);
    }
}
