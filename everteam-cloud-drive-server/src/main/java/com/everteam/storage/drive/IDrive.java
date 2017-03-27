package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import com.everteam.storage.common.FileMetadata;
import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;

public interface IDrive {

    ESFileList children( String parentId, boolean addPermissions, boolean addChecksum, int maxSize ) throws IOException;
    
    void downloadTo(String fileId,  OutputStream outputstream) throws IOException ;
    /**
     * Insert file into directory.<br> 
     * <b>We guarantee that parentId is a directory</b>
     * @param parentId
     * @param metadata
     * @param in
     * @return new File Id
     * @throws IOException
     */
    String insertFile(String parentId, FileMetadata metadata, InputStream in) throws IOException;
    /**
     * Insert folder into directory.<br> 
     * <b>We guarantee that parentId is a directory</b>
     * @param parentId
     * @param name
     * @param description
     * @return new Folder Id
     * @throws IOException
     */
    String insertFolder(String parentId, String name, String description) throws IOException;
    
    boolean isFolder(String fileId) throws IOException;

    void init(ESRepository repository) throws IOException;

    ESRepository getRepository() throws IOException;

    List<ESPermission> getPermissions(String fileId) throws IOException;

    void delete(String fileId) throws IOException;

    ESFile getFile(String fileId, boolean addPermissions, boolean addChecksum) throws IOException;

    void update(String fileId, String name, String contentType, InputStream in, String description) throws IOException;

    void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException;

}
