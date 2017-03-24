package com.everteam.storage.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.drive.GoogleDrive;
import com.everteam.storage.drive.IDrive;
import com.everteam.storage.drive.FSDrive;

@Component
public class RepositoryService {
    private final static Logger LOG = LoggerFactory.getLogger(RepositoryService.class);

    @Autowired 
    BeanFactory beanFactory;

    public static Map<String, IDrive> drives = new HashMap<>();

    public IDrive getDrive(String id) {
        return drives.get(id);
    }

    public List<IDrive> getDriveList() {
        return new ArrayList<>(drives.values());

    }

    public List<ESRepository> getRepositoryList() throws Exception {
        List<ESRepository> repositories = new ArrayList<>();
        List<IDrive> connectors = getDriveList();
        for (IDrive connector : connectors) {
            repositories.add(connector.getRepository());
        }
        return repositories;
    }

    public void startRepository(ESRepository repository) {
        IDrive drive = getDrive(repository.getName());
        if (drive == null) {
            switch (repository.getType()) {
            case GOOGLE:
                drive = (IDrive) beanFactory.getBean("google");
                break;
            case FS:
                drive = (IDrive) beanFactory.getBean("fs");
                break;
            default:
                break;
            }
            if (drive != null) {
                try {
                    repository.setId(new ESFileId().repositoryName(repository.getName()));
                    drive.init(repository);
                    
                    drives.put(repository.getName(), drive);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }

            }
        }

    }

}
