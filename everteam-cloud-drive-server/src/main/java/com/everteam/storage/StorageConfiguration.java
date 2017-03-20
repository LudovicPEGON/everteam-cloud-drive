package com.everteam.storage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.services.RepositoryService;

@Component
@ConfigurationProperties(prefix = "storage")
public class StorageConfiguration {
    private List<ESRepository> repositories = new ArrayList<>();

    @Autowired
    RepositoryService storageManager;
        
    
    @PostConstruct
    public void init() {
        
            for (ESRepository current : this.getRepositories()) {
                storageManager.startRepository(current);
            }
        
    }

    public List<ESRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<ESRepository> repositories) {
        this.repositories = repositories;
    }
}