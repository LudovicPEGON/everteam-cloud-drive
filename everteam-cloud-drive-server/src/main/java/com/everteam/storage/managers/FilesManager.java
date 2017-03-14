package com.everteam.storage.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.connector.IConnector;

@Component
public class FilesManager {

    @Autowired
    ConnectorsManager connectorManager;

    public ESFile getFile(FileId fileId) {
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());
        return connector.getFile(fileId.getRepositoryFileId());
    }

    public void copy(ESFile source, ESFile target) throws IOException {
        FileId sourceId = FileId.get(source.getId());
        IConnector sourceConnector = connectorManager.getConnector(sourceId.getRepositoryId());

        FileId targetId = FileId.get(target.getId());
        IConnector targetConnector = connectorManager.getConnector(targetId.getRepositoryId());

        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        sourceConnector.downloadTo(sourceId.getRepositoryFileId(), baOS);
        targetConnector.insert(target, new ByteArrayInputStream(baOS.toByteArray()));

    }

    public ESFileList getChildren(String id) throws IOException {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());
        return connector.children(fileId.getRepositoryFileId());
    }

    public void delete(String id) throws IOException {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());

        connector.delete(fileId.getRepositoryFileId());
    }

    public List<ESPermission> getPermissions(String id) {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());

        return connector.getPermissions(fileId.getRepositoryFileId());
    }

    public void downloadTo(String id, OutputStream outputStream) throws IOException {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());

        connector.downloadTo(fileId.getRepositoryFileId(), outputStream);

    }

    public byte[] getFileContent(String id) throws IOException {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());

        ByteArrayOutputStream baOS = new ByteArrayOutputStream();
        connector.downloadTo(fileId.getRepositoryFileId(), baOS);
        return baOS.toByteArray();

    }

    public void update(String id, MultipartFile content, String name, String description) throws IOException {
        FileId fileId = FileId.get(id);
        IConnector connector = connectorManager.getConnector(fileId.getRepositoryId());
        connector.update(fileId, content, name, description);
    }

    public FileId create(ESFile file, InputStream inputStream) throws IOException {
        FileId parentId = FileId.get(file.getParents().get(0).getId());
        IConnector connector = connectorManager.getConnector(parentId.getRepositoryId());
        return connector.insert(file, inputStream);
        
    }

}
