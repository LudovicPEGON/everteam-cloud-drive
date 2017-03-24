package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;

public interface IDrive {

    ESFileList children( ESFileId parentId, boolean addPermissions, boolean addChecksum, int maxSize ) throws IOException;
    
    void downloadTo(ESFileId fileId,  OutputStream outputstream) throws IOException ;
    
    ESFileId insert(ESFileId parentId, String name, String contentType, InputStream in, String description) throws IOException;

    void init(ESRepository repository) throws IOException;

    ESRepository getRepository() throws IOException;

    List<ESPermission> getPermissions(ESFileId fileId) throws IOException;

    void delete(ESFileId fileId) throws IOException;

    ESFile getFile(ESFileId fileId, boolean addPermissions, boolean addChecksum) throws IOException;

    void update(ESFileId fileId, String name, String contentType, InputStream in, String description) throws IOException;

    void checkUpdates(ESFileId fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException;

}
