package com.everteam.storage.connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;

@Component
@Scope("prototype")
public class LocalConnector extends ConnectorImpl {

    private final static Logger LOG = LoggerFactory.getLogger(LocalConnector.class);

    
    @Value("${storage.local.maxFilesInDirectory:#{1000}}")
    private int maxFilesInDirectory;

    @Value("${storage.local.keepEmptyDirectory:#{false}}")
    private boolean keepEmptyDirectory;


    @Value("${storage.local.uri.depth:#{4}}")
    private int fileDepth;


    @PostConstruct
    public void init() {
    }

    @Override
    public ESFileList children(String parentId) throws IOException {
        Path p = buildPath(parentId);
        
        List<ESFile> items = new ArrayList<>();
        Files.list(p).forEach(new Consumer<Path>() {
            @Override
            public void accept(Path t) {
                
                File file = t.toFile();

                
                
                AclFileAttributeView aclView = Files.getFileAttributeView(t, AclFileAttributeView.class);
                
                try {
                    for (AclEntry entry : aclView.getAcl()) {
                        LOG.debug(entry.principal().getName() + " - [" + entry.type() +  "] : \t");
                    }
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                
                //get mime_type
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String type = fileNameMap.getContentTypeFor(file.getName());                
                
                //build a ESFile from file object
                ESFile esfile = new ESFile()
                        .id(file.getName())
                        .name(file.getName())
                        .mimeType(type)
                        .directory(file.isDirectory());
                
                if (file.isDirectory()) {
                    esfile.fileSize(file.length());
                }
               
                //fill date-time properties from attributes.
                try {
                    BasicFileAttributes attrs = Files.readAttributes(t, BasicFileAttributes.class);
                    FileTime lastAccessTime = attrs.lastAccessTime();
                    esfile.lastAccessTime(OffsetDateTime.ofInstant(lastAccessTime.toInstant(), ZoneOffset.UTC));
                    
                    FileTime lastModifiedTime = attrs.lastModifiedTime();
                    esfile.lastModifiedTime(OffsetDateTime.ofInstant(lastModifiedTime.toInstant(), ZoneOffset.UTC));
                    
                    FileTime creationTime = attrs.creationTime();
                    esfile.creationTime(OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneOffset.UTC));
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                
                items.add(esfile);
            }
        });
        
        return new ESFileList().items(items);
    }

    @Override
    public void downloadTo(String fileId, OutputStream outputstream) throws IOException {
        Path p = buildPath(fileId);
        Files.copy(p, outputstream);        
    }

    private Path buildPath(String fileId) {
        Path p;
        ESRepository repository = getRepository();
        p = Paths.get(repository.getRootDirectory(), fileId);
        return p;
    }

    @Override
    public void insert(ESFile file, InputStream in) {
        //file.getParents().get(0).
        
    }

    @Override
    public List<ESPermission> getPermissions(String repositoryFileId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String repositoryFileId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ESFile getFile(String repositoryFileId) {
        // TODO Auto-generated method stub
        return null;
    }

   
    
    
    
}
