package com.everteam.storage.drive;

import java.io.ByteArrayOutputStream;
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
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESPermission.AccountTypeEnum;
import com.everteam.storage.common.model.ESPermission.RolesEnum;
import com.everteam.storage.common.model.ESPermission.TypeEnum;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.common.model.ESUser;

@Component(value="fs")
@Scope("prototype")
public class FSDrive extends DriveImpl {

    private final static Logger LOG = LoggerFactory.getLogger(FSDrive.class);

    @Value("${storage.fs.maxFilesInDirectory:#{1000}}")
    private int maxFilesInDirectory;

    @Value("${storage.fs.keepEmptyDirectory:#{false}}")
    private boolean keepEmptyDirectory;

    @Value("${storage.fs.uri.depth:#{4}}")
    private int fileDepth;

    @PostConstruct
    public void init() {
    }

    @Override
    public ESFileList children(ESFileId parentId, boolean addPermissions, boolean addChecksum, int maxSize) throws IOException {
        Path p = buildPath(parentId);

        List<ESFile> items = new ArrayList<>();

        try (Stream<Path> paths = Files.list(p)) {
            paths.limit(maxSize).forEach(new Consumer<Path>() {
                @Override
                public void accept(Path t) {
                    try {
                        items.add(getFile(t, addPermissions));
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            });
        }

        return new ESFileList().items(items);
    }

    @Override
    public void downloadTo(ESFileId fileId, OutputStream outputstream) throws IOException {
        Path p = buildPath(fileId);
        Files.copy(p, outputstream);
    }

    @Override
    public ESFileId insert(ESFileId fileId, String name, String contentType, InputStream in, String description) throws IOException {
        ESFileId newId = null;
        Path path = buildPath(fileId);
        if (path.toFile().isDirectory()) {
            // TODO : add some extension based on +"."+file.getMimeType()
            Path newFilePath = path.resolve(name);
            Files.copy(in, newFilePath);
            newId = buildFileId(newFilePath);

        } else {
            throw new IOException("CannotInsertFileInNonDirectory");
        }
        return newId;
    }

    @Override
    public List<ESPermission> getPermissions(ESFileId fileId) throws IOException {
        return getPermissions(buildPath(fileId));
    }

    @Override
    public void delete(ESFileId repositoryFileId) throws IOException {
        Path path = buildPath(repositoryFileId);
        deleteAllInPath(path);
    }

    @Override
    public ESFile getFile(ESFileId fileId, boolean addPermissions,  boolean addChecksum) throws IOException {
        Path path = buildPath(fileId);
        return getFile(path, addPermissions);
    }

    @Override
    public void update(ESFileId fileId, String name, String contentType, InputStream in, String description) throws IOException {
        Path path = buildPath(fileId);
        Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        //description can't be manage is this drive 
    }

    @Override
    public void checkUpdates(ESFileId fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        Path start = buildPath(fileId);
        
        try (Stream<Path> paths = Files
                .find(start, 20, (path, attr) -> !attr.isDirectory() && 
        OffsetDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneOffset.UTC).isAfter(fromDate))) {
            paths.forEach(new Consumer<Path>() {
                @Override
                public void accept(Path t) {
                    try {
                        
                        consumer.accept(getFile(buildFileId(t), false, false));
                        
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            });
        }
    }
    
    
    
    
    
    private ESFile getFile(Path path, boolean addPermissions) throws IOException {
        // get mime_type
        File file = path.toFile();

        if (!file.exists()) {
            throw new IOException("FileDoesNotExists");
        }
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getName());

        // build a ESFile from file object
        ESFile esfile = new ESFile()
                .id(new ESFileId().repositoryName(getRepository().getName())
                        .path(buildRelativePath(path).toString()))
                .name(file.getName()).mimeType(type).directory(file.isDirectory());

        if (!file.isDirectory()) {
            esfile.fileSize(file.length());

            String md5 = DigestUtils.md5DigestAsHex(getFileContent(path));
            esfile.setChecksum(md5);
        }

        // fill date-time properties from attributes.
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

        FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        UserPrincipal owner = ownerAttributeView.getOwner();
        esfile.addOwnersItem(new ESUser().id(owner.getName()).displayName(owner.toString()));

        if (addPermissions) {
            esfile.setPermissions(getPermissions(path));
        } else {
            esfile.setPermissions(null);
        }

        Path repositoryPath = Paths.get(repository.getRootDirectory());
        Path parentPath = path.getParent();
        Path relativePath = buildRelativePath(parentPath);

        List<String> paths = new ArrayList<>();
        if (relativePath.toString().length() > 0) {
            relativePath.forEach(new Consumer<Path>() {
                @Override
                public void accept(Path t) {
                    paths.add(t.toString());
                }
            });
        }

        if (parentPath.startsWith(repositoryPath)) {
            esfile.addParentsItem(new ESParent()
                    .id(new ESFileId().repositoryName(getRepository().getName()).path(relativePath.toString()))
                    .paths(paths));
        }

        return esfile;
    }

    private Path buildPath(ESFileId fileId) {
        Path p;
        ESRepository repository = getRepository();
        p = Paths.get(repository.getRootDirectory());
        if (fileId != null) {
            p = p.resolve(fileId.getPath());
        }
        return p;
    }

    private ESFileId buildFileId(Path filePath) throws IOException {
        Path relativePath = buildRelativePath(filePath);
        return new ESFileId().repositoryName(this.getRepository().getName()).path(relativePath.toString());
    }

    private Path buildRelativePath(Path filePath) throws IOException {
        return Paths.get(this.getRepository().getRootDirectory()).relativize(filePath);
    }

    private void deleteAllInPath(Path directory) throws IOException {
        if (directory != null) {
            Files.walk(directory, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
                    .forEach(File::delete);
            Files.deleteIfExists(directory);
        }
    }

    private List<ESPermission> getPermissions(Path p) throws IOException {
        List<ESPermission> permissions = new ArrayList<>();
        AclFileAttributeView aclView = Files.getFileAttributeView(p, AclFileAttributeView.class);
        for (AclEntry entry : aclView.getAcl()) {

            TypeEnum type = null;
            switch (entry.type()) {
            case ALLOW:
                type = TypeEnum.ALLOW;
                break;
            case DENY:
                type = TypeEnum.DENY;
                break;
            default:
                break;
            }
            if (type != null) {
                TypeEnum f_type = type;
                List<RolesEnum> roles = new ArrayList<>();
                if (entry.permissions().contains(AclEntryPermission.READ_DATA)) {
                    roles.add(RolesEnum.READER);
                }
                if (entry.permissions().contains(AclEntryPermission.WRITE_DATA)) {
                    roles.add(RolesEnum.WRITER);
                }
                if (entry.permissions().contains(AclEntryPermission.WRITE_OWNER)) {
                    roles.add(RolesEnum.OWNER);
                }

                if (roles.size() > 0) {
                    ESPermission permission = new ESPermission().type(f_type).userId(entry.principal().getName())
                            .accountType(AccountTypeEnum.USER).roles(roles);
                    permissions.add(permission);
                }

            }
        }
        return permissions;
    }

    private byte[] getFileContent(Path path) throws IOException {
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        Files.copy(path, outputstream);
        return outputstream.toByteArray();
    }


}
