package com.everteam.storage.drive;

import com.everteam.storage.common.model.ESRepository;

public abstract class DriveImpl implements IDrive {
    ESRepository repository;

    @Override
    public void init(ESRepository repository) {
        this.repository = repository;
    }

    @Override
    public ESRepository getRepository() {
        return repository;
    }
    
    

}
