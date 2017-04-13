package com.everteam.storage.drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.utils.Messages;

public abstract class DriveImpl implements IDrive {
    ESRepository repository;

    
    @Autowired
    Messages messages;
    
    @Override
    public void init(ESRepository repository) throws IOException, GeneralSecurityException {
        this.repository = repository;
    }

    @Override
    public ESRepository getRepository() {
        return repository;
    }
    
    

}
