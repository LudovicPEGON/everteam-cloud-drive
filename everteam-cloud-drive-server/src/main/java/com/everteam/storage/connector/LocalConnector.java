package com.everteam.storage.connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileVisitOption;
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
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.managers.FileId;

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
                
                AclFileAttributeView aclView = Files.getFileAttributeView(t, AclFileAttributeView.class);
                
                try {
                    for (AclEntry entry : aclView.getAcl()) {
                        LOG.debug(entry.principal().getName() + " - [" + entry.type() +  "] : \t");
                    }
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                
                ESFile esfile = getFile(t);
                
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

    
    @Override
    public FileId insert(ESFile file, InputStream in) throws IOException {
        FileId newId = null;
        FileId parentId = FileId.get(file.getParents().get(0).getId());
        Path path = buildPath(parentId.getRepositoryFileId());
        if (path.toFile().isDirectory()) {
            // TODO : add some extension based on +"."+file.getMimeType()
            Path newFilePath = path.resolve(file.getName()); 
            Files.copy(in, newFilePath);
            newId = new FileId().repositoryId(parentId.getRepositoryId())
                    .repositoryFileId(getFileId(newFilePath));
        }
        else {
            throw new IOException("CannotInsertFileInNonDirectory");
        }
        return newId;
    }

    @Override
    public List<ESPermission> getPermissions(String repositoryFileId) {
        // TODO Auto-generated method stub
        // http://stackoverflow.com/questions/10783677/how-to-check-file-permissions-in-java-os-independently
        return null;
    }

    @Override
    public void delete(String repositoryFileId) throws IOException {
        Path path = buildPath(repositoryFileId);
        deleteAllInPath(path);
    }

    @Override
    public ESFile getFile(String repositoryFileId) {
        Path path = buildPath(repositoryFileId);
        return getFile(path);
    }
    
    @Override
    public void update(FileId fileId, MultipartFile content, String name, String description) {
        
        
    }

    private ESFile getFile(Path path) {
        // get mime_type
        File file = path.toFile();

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getName());

        //build a ESFile from file object
        ESFile esfile = new ESFile()
                .id(FileId.toURI(this, getFileId(path)).toString())
                .name(file.getName())
                .mimeType(type)
                .directory(file.isDirectory());

        if (file.isDirectory()) {
            esfile.fileSize(file.length());
        }

        //fill date-time properties from attributes.
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime lastAccessTime = attrs.lastAccessTime();
            esfile.lastAccessTime(OffsetDateTime.ofInstant(lastAccessTime.toInstant(), ZoneOffset.UTC));

            FileTime lastModifiedTime = attrs.lastModifiedTime();
            esfile.lastModifiedTime(OffsetDateTime.ofInstant(lastModifiedTime.toInstant(), ZoneOffset.UTC));

            FileTime creationTime = attrs.creationTime();
            esfile.creationTime(OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneOffset.UTC));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return esfile;
    }

    private Path buildPath(String fileId) {
        Path p;
        ESRepository repository = getRepository();
        p = Paths.get(repository.getRootDirectory());
        if (fileId != null) {
            p = p.resolve(fileId);
        }
        return p;
    }
    private String getFileId(Path filePath){
        return Paths.get(this.getRepository().getRootDirectory())
                .relativize(filePath).toString();
    }

    private void deleteAllInPath(Path directory) throws IOException {
        if (directory != null) {
            Files.walk(directory, FileVisitOption.FOLLOW_LINKS)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
            Files.deleteIfExists(directory);
        }
    }

    
}
