package com.everteam.storage.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.drive.IDrive;

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
        List<IDrive> drives = getDriveList();
        for (IDrive drive : drives) {
            ESRepository repository =  drive.getRepository();
            ESRepository cloneRepository =  new ESRepository()
                    .id(repository.getId())
                    .name(repository.getName())
                    .rootDirectory(repository.getRootDirectory())
                    .type(repository.getType())
                    .clientSecret(repository.getClientSecret());
            
            // we need to clone repository, because they are static and their id are modified by serializer.
            repositories.add(cloneRepository);
        }
        return repositories;
    }

    public void startRepository(ESRepository repository) {
        IDrive drive = drives.get(repository.getName());
        if (drive == null) {
            switch (repository.getType()) {
            case GOOGLE:
                drive = (IDrive) beanFactory.getBean("google");
                break;
            case FS:
                drive = (IDrive) beanFactory.getBean("fs");
                break;
            case ONEDRIVE:
                drive = (IDrive) beanFactory.getBean("onedrive");
                break;
            default:
                break;
            }
            if (drive != null) {
                try {
                    repository.setId(repository.getName());
                    drive.init(repository);
                    
                    drives.put(repository.getName(), drive);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                } catch (GeneralSecurityException e) {
                    LOG.error(e.getMessage(), e);
                }

            }
        }

    }

}
