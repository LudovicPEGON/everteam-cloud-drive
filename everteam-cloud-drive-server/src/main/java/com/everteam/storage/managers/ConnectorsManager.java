package com.everteam.storage.managers;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.connector.GoogleConnector;
import com.everteam.storage.connector.IConnector;
import com.everteam.storage.connector.LocalConnector;

@Component
public class ConnectorsManager {
    @Autowired
    BeanFactory beanFactory;
   

    public static Map<String, IConnector> connectors = new HashMap<>();
   

    public IConnector getConnector(String id) {
        return connectors.get(id);
    }

    public List<IConnector> getConnectorList() {
        return new ArrayList<>(connectors.values());

    }
    
    
    public List<ESRepository> getRepositoryList() {
        List<ESRepository> repositories = new ArrayList<>();
        List<IConnector> connectors = getConnectorList();
        for (IConnector connector : connectors) {
            repositories.add(connector.getRepository());
        }
        return repositories;
    }
    
    

    public void startRepository(ESRepository repository) {
        IConnector storage = getConnector(repository.getName());
        if (storage == null) {
            switch (repository.getType()) {
                case GOOGLE:
                    storage = beanFactory.getBean(GoogleConnector.class);
                    break;
                case LOCAL:
                    storage = beanFactory.getBean(LocalConnector.class);
                    break;
                default:
                    break;
            }
            if (storage != null) {
                repository.setId(repository.getName() + ":/");
                storage.init(repository);
                
                connectors.put(repository.getId(), storage);
            }
        }

    }

    public ESFileList getFiles(String id, Boolean getPermissions, Integer maxResult) throws IOException {
        IConnector connector = getConnector(id);
        return connector.children(null);
    }

}
