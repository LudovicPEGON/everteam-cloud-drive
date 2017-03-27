package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;

public interface IDrive {

    ESFileList children( String parentId, boolean addPermissions, boolean addChecksum, int maxSize ) throws IOException;
    
    void downloadTo(String fileId,  OutputStream outputstream) throws IOException ;
    
    String insert(String parentId, String name, String contentType, InputStream in, String description) throws IOException;

    void init(ESRepository repository) throws IOException;

    ESRepository getRepository() throws IOException;

    List<ESPermission> getPermissions(String fileId) throws IOException;

    void delete(String fileId) throws IOException;

    ESFile getFile(String fileId, boolean addPermissions, boolean addChecksum) throws IOException;

    void update(String fileId, String name, String contentType, InputStream in, String description) throws IOException;

    void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException;

}
