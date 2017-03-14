package com.everteam.storage.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.managers.FileId;

public interface IConnector {

   

    ESFileList children( String parentId ) throws IOException;
    
    void downloadTo(String fileId,  OutputStream outputstream) throws IOException ;
    
    FileId insert( ESFile file, InputStream in) throws IOException;

    void init(ESRepository repository);

    ESRepository getRepository();

    List<ESPermission> getPermissions(String repositoryFileId);

    void delete(String repositoryFileId) throws IOException;

    ESFile getFile(String repositoryFileId);

    void update(FileId fileId, MultipartFile content, String name, String description);

}
