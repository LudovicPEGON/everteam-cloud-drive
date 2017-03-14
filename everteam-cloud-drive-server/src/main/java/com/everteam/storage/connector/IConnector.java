package com.everteam.storage.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;

public interface IConnector {

   

    ESFileList children( String parentId ) throws IOException;
    
    void downloadTo(String fileId,  OutputStream outputstream) throws IOException ;
    
    void insert( ESFile file, InputStream in);

    // String storeContent(ESFile file, InputStream inputStream);
    //
    // void delete(String uri);
    //
    // String rename(String uri, String newFileName);
    //
    // String copy(String uri, ESFile destinationFileInfo);
    //
    // void update(String fileName, String uri, InputStream inputStream);
    //
    // // FileStream getContent(String uri);
    //
    void init(ESRepository repository);

    ESRepository getRepository();

    List<ESPermission> getPermissions(String repositoryFileId);

    void delete(String repositoryFileId);

    ESFile getFile(String repositoryFileId);

}
