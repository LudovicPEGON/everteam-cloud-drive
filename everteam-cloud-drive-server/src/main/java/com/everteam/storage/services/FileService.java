package com.everteam.storage.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.drive.IDrive;
import com.everteam.storage.jackson.FileIdSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FileService {

    private final static Logger LOG = LoggerFactory.getLogger(FileService.class);

    @Value("${storage.watcherDirectory:#{'d:/temp2'}}")
    private String watcherDirectory;

    @Autowired
    RepositoryService driveManager;
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    

    public ESFile getFile(ESFileId fileId, boolean addPermissions, Boolean addChecksum) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());
        return drive.getFile(fileId.getPath(), addPermissions, addChecksum)
                .repositoryId(fileId.getRepositoryName());
    }

    public ESFileId copy(ESFileId sourceId, ESFileId targetId) throws IOException {
        IDrive sourcedrive = driveManager.getDrive(sourceId.getRepositoryName());

        IDrive targetDrive = driveManager.getDrive(targetId.getRepositoryName());

        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        sourcedrive.downloadTo(sourceId.getPath(), baOS);
        ESFile sourceFile = sourcedrive.getFile(sourceId.getPath(), false, false);
        String newFileId = targetDrive.insert(targetId.getPath(),  
                sourceFile.getName(), 
                sourceFile.getMimeType(), 
                new ByteArrayInputStream(baOS.toByteArray()), sourceFile.getDescription());
        return new ESFileId()
                .repositoryName(targetId.getRepositoryName())
                .path(newFileId);
        
        
    }

    public ESFileList getChildren(ESFileId fileId, boolean addPermissions, Boolean addChecksum, int maxSize) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());
        ESFileList files = drive.children(fileId.getPath(), addPermissions, addChecksum, maxSize);
        for (ESFile file : files.getItems()) {
            file.setRepositoryId(fileId.getRepositoryName());
        }
        return files;
        
        
    }

    public void delete(ESFileId fileId) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());
        drive.delete(fileId.getPath());
    }

    public List<ESPermission> getPermissions(ESFileId fileId) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());

        return drive.getPermissions(fileId.getPath());
    }

    public void downloadTo(ESFileId fileId, OutputStream outputStream) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());

        drive.downloadTo(fileId.getPath(), outputStream);

    }

    public byte[] getFileContent(ESFileId fileId) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());

        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        drive.downloadTo(fileId.getPath(), baOS);
        return baOS.toByteArray();

    }

    public void update(ESFileId fileId, String name, String contentType, InputStream in, String description) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());
        drive.update(fileId.getPath(), name, contentType, in,  description);
    }

    public ESFileId create(ESFileId parentId, String name, String contentType, InputStream in, String description)
            throws IOException {
        IDrive drive = driveManager.getDrive(parentId.getRepositoryName());
        String newFileId =  drive.insert(parentId.getPath(), name, contentType, in, description);
        return new ESFileId()
                .repositoryName(parentId.getRepositoryName())
                .path(newFileId);
    }

    public void checkUpdates(ESFileId fileId, OffsetDateTime fromDate) throws IOException {
        IDrive drive = driveManager.getDrive(fileId.getRepositoryName());
        CheckUpdate cu = new CheckUpdate(this, drive, fileId.getPath(), fromDate);
        Thread thread = new Thread(cu);
        thread.start();
    }

    public void exportWatcherFile(ESFile file) {
        try {
            Path targetDir = Paths.get(watcherDirectory)
            .resolve(file.getRepositoryId())
            .resolve(String.valueOf(file.getLastModifiedTime().getYear()))
            .resolve(String.valueOf(file.getLastModifiedTime().getMonth()))
            .resolve(String.valueOf(file.getLastModifiedTime().getDayOfMonth()));
            
            targetDir.toFile().mkdirs();
            
            
            Path target = targetDir.resolve(FileIdSerializer.encrypt(
                    new ESFileId()
                        .repositoryName(file.getRepositoryId())
                        .path(file.getId())
                    ));            
            
            jacksonObjectMapper.writeValue(target.toFile(), file);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static class CheckUpdate implements Runnable {
        private final static Logger LOG = LoggerFactory.getLogger(CheckUpdate.class);

        FileService service;
        IDrive drive;
        String fileId;
        OffsetDateTime fromDate;

        public CheckUpdate(FileService service, IDrive drive, String fileId, OffsetDateTime fromDate) {
            this.service= service;
            this.drive = drive;
            this.fileId = fileId;
            this.fromDate = fromDate;
        }

        @Override
        public void run() {
            try {
                drive.checkUpdates(fileId, fromDate, new FileConsumer( service, drive.getRepository().getId()));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private static class FileConsumer implements Consumer<ESFile> {
        FileService fileService;
        String repositoryId;
        
        public FileConsumer(FileService fileService, String repositoryId) {
            this.fileService = fileService;
            this.repositoryId = repositoryId;
        }
        
        @Override
        public void accept(ESFile file) {
            fileService.exportWatcherFile(file.repositoryId(repositoryId));
            
        }

      
        
        
    }

}
