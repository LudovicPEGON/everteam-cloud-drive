package com.everteam.storage.drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.everteam.storage.common.model.ESRepository;

public abstract class DriveImpl implements IDrive {
    ESRepository repository;

    @Override
    public void init(ESRepository repository) throws IOException, GeneralSecurityException {
        this.repository = repository;
    }

    @Override
    public ESRepository getRepository() {
        return repository;
    }
    
    

}
