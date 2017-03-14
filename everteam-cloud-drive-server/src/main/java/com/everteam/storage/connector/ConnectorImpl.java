package com.everteam.storage.connector;

import com.everteam.storage.common.model.ESRepository;

public abstract class ConnectorImpl implements IConnector {
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
