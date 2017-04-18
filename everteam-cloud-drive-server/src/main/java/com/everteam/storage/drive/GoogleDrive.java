package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESPermission.AccountTypeEnum;
import com.everteam.storage.common.model.ESPermission.RolesEnum;
import com.everteam.storage.common.model.ESPermission.TypeEnum;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.common.model.ESUser;
import com.everteam.storage.utils.FileInfo;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.drive.model.User;

@Component(value = "google")
@Scope("prototype")
@ConfigurationProperties(prefix = "google")
public class GoogleDrive extends OAuth2DriveImpl {

    private final static Logger LOG = LoggerFactory.getLogger(GoogleDrive.class);
    
    private final ConcurrentHashMap<String, File> cache = new ConcurrentHashMap<>();

    protected AuthorizationCodeResourceDetails client;
    
    protected ResourceServerProperties resource;
    
    private Drive drive;

    /** Application name. */
    private static final String APPLICATION_NAME = "everteam-ms-storage";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;


    @Override
    public void init(ESRepository repository) throws IOException, GeneralSecurityException {
        super.init(repository);
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);

        }
        
    }

    @Override
    public ESFileList children(String parentId, boolean addPermissions, boolean addChecksum, int maxSize) throws IOException {
        ESFileList result = new ESFileList();
        Drive drive = getDriveService();

        Files.List request = drive.files().list().setPageSize(maxSize)
                // .setFields("id,md5Checksum,createdTime,description,mimeType,size,modifiedTime,lastModifyingUser,mimeType,name");
                .setFields("*");
        String relativeParentId = parentId;
        if (relativeParentId == null || relativeParentId.length() == 0) {
            relativeParentId = repository.getRootDirectory();
        }
        
        if (relativeParentId != null && relativeParentId.length() > 0) {
            request.setQ("'" + relativeParentId + "' in parents");
        }
        FileList files = request.execute();

        for (File f : files.getFiles()) {
            result.addItemsItem(buildFile(drive, f, addPermissions,addChecksum));
        }

        return result;

    }

    @Override
    public void downloadTo(String fileId, OutputStream outputstream) throws IOException {
        getDriveService().files().get(fileId).executeMediaAndDownloadTo(outputstream);
    }

    @Override
    public List<ESPermission> getPermissions(String fileId) throws IOException {
        PermissionList permissions = getDriveService().permissions().list(fileId).execute();
        return buildPermissions(permissions.getPermissions());
    }

    @Override
    public void delete(String fileId) throws IOException {
        getDriveService().files().delete(fileId).execute();

    }

    @Override
    public ESFile getFile(String fileId, boolean addPermissions, boolean addChecksum) throws IOException {
        Drive drive = getDriveService();
        if (StringUtils.isEmpty(fileId)) {
            if (!StringUtils.isEmpty(this.getRepository().getRootDirectory())){
                fileId = getRepository().getRootDirectory();
            }
            else {
                fileId = "root";
            }
        }
        Files.Get request = drive.files().get(fileId);
        request.setFields("*");
        return buildFile(drive, request.execute(), addPermissions, addChecksum);
    }

    @Override
    public void update(String fileId, FileInfo info) throws IOException {
        // First retrieve the file from the API.
        File file = new File();

        // File's new metadata.
        // file.setName(newName);
        file.setDescription(info.getDescription());
        // file.setMimeType(newMimeType);

        InputStreamContent mediaContent = new InputStreamContent(info.getMimeType(), info.getInputStream());

        // Send the request to the API.
        getDriveService().files().update(fileId, file, mediaContent).execute();
    }

    @Override
    public String insertFile(String parentId, FileInfo info) throws IOException {
        return insert(parentId, info.getName(), info.getMimeType(), info.getInputStream(), info.getDescription());
    }

    @Override
    public String insertFolder(String parentId, String name, String description) throws IOException {
        return insert(parentId, name, null, null, description);
    }
    
    @Override
    public boolean isFolder(String fileId) throws IOException {
        if (fileId == null ||fileId.isEmpty()) {
            fileId = repository.getRootDirectory(); 
        }
        Files.Get request = getDriveService().files().get(fileId);
        request.setFields("mimeType");
        File file = request.execute();
        return file.getMimeType().equals("application/vnd.google-apps.folder");
    }
    
    
    @Override
    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        Drive drive = getDriveService();
        Files.List request = drive.files()
                .list()
                .setPageSize(100)
                .setFields("*")
                .setOrderBy("modifiedTime")
                .setQ("modifiedTime > '" + fromDate + "' and mimeType != 'application/vnd.google-apps.folder'");
        
        do {
              FileList files = request.execute();
              files.getFiles().iterator().forEachRemaining(new Consumer<File>() {
                  @Override
                  public void accept(File t) {
                      try {
                          consumer.accept(buildFile(drive, t, true,true));
                      } catch (IOException e) {
                          LOG.error(e.getMessage(), e);
                      }
                      
                  }
              });
              request.setPageToken(files.getNextPageToken());
          } while (request.getPageToken() != null &&
                   request.getPageToken().length() > 0);
    }
    
    

    /**
     * Build and return an authorized Drive client service.
     * 
     * @return an authorized Drive client service
     * @throws IOException
     */
    private Drive getDriveService() throws IOException {
        return drive;
    }

    
    public String insert(String parentId, String name, String contentType, InputStream in, String description) throws IOException {
        // File's metadata.
        File body = new File();
        body.setName(name);
        body.setDescription(description);
        

        // Set the parent folder.
        String pId = parentId;
        if (pId == null ||pId.isEmpty()) {
            pId = repository.getRootDirectory(); 
        }
        if (pId != null && pId.length() > 0) {
          body.setParents(Arrays.asList(pId));
        }
       File file = null;
        if (in != null) {
            // File's content.
            body.setMimeType(contentType);
            InputStreamContent mediaContent = new InputStreamContent(contentType, in); 
            file = getDriveService().files().create(body, mediaContent).execute();
        }
        else {
            // it's a folder
            body.setMimeType("application/vnd.google-apps.folder");
            file = getDriveService().files().create(body).execute();
        }
        
        if (file==null) {
            throw new IOException(messages.get("error.item.cannotcreate"));
        }
        
        return file.getId();
    }
    
    @Override
    public boolean exists(String fileId) throws IOException{
        boolean bret = false; 
        Files.Get request = getDriveService().files().get(fileId);
        try {
            request.execute();
            bret = true;
        } 
        catch (GoogleJsonResponseException e) {
            if (e.getDetails().getCode() == 404) {
                bret = false;
            }
            else {
                throw e;
            }
            
        }
        return bret;
    }

    
    
    

    private ESFile buildFile(Drive drive, File file, boolean addPermissions, boolean addChecksum) throws IOException {
        ESUser lastModifiedUser = null;
        User lastModifyingUser = file.getLastModifyingUser();
        if (lastModifyingUser != null) {
            lastModifiedUser = new ESUser().id(lastModifyingUser.getEmailAddress())
                    .displayName(lastModifyingUser.getDisplayName());
        }

        ESFile esfile = new ESFile()
                .id(file.getId())
                .creationTime(OffsetDateTime.parse(file.getCreatedTime().toStringRfc3339()))
                .description(file.getDescription())
                .directory(file.getMimeType().equals("application/vnd.google-apps.folder")).fileSize(file.getSize())
                .lastAccessTime(null).lastModifiedTime(OffsetDateTime.parse(file.getModifiedTime().toStringRfc3339()))
                .lastModifiedUser(lastModifiedUser).mimeType(file.getMimeType()).name(file.getName()).owners(null);
        
        if (addChecksum) {
            esfile.checksum(file.getMd5Checksum());
        }
        
        if (addPermissions) {
                List<ESPermission> permissions = buildPermissions(file.getPermissions());
                esfile.setPermissions(permissions);
        } else {
            esfile.setPermissions(null);
        }

        List<String> parents = file.getParents();
        if (parents != null) {
            for (String parent : parents) {
                esfile.addParentsItem(new ESParent()
                        .id(parent)
                        .paths(absPath(drive, file)));
            }
        }
        return esfile;
    }

    private List<ESPermission> buildPermissions(List<Permission> permissions) {
        List<ESPermission> permissionss = new ArrayList<>();
        if (permissions != null) {
            for (Permission p : permissions) {
                permissionss.add(new ESPermission().accountType(AccountTypeEnum.fromValue(p.getType()))
                        .domain(p.getDomain()).addRolesItem(RolesEnum.fromValue(p.getRole()))
                        .type(TypeEnum.ALLOW).userId(p.getEmailAddress()));
                
    
            }
        }
        return permissionss;
    }
    
    
    
    
    private List<String> absPath(Drive drive, File file) throws IOException {
        List<String> path = new ArrayList<>();

        while (true) {
            File parent = getFile(drive, file.getParents().get(0));

            // Stop when we find the root dir

            if (parent.getParents() == null || parent.getParents().size() == 0
                    || parent.getId().equals(getRepository().getRootDirectory())) {
                break;
            }

            path.add(0, parent.getName());
            file = parent;
        }
        // path.add(name);
        return path;
    }

    private File getFile(Drive drive, String id) throws IOException {
        // Fetch file from drive
        File file = cache.get(id);
        if (file == null) {
            Files.Get request = drive.files().get(id);
            request.setFields("id,name,parents");
            file = request.execute();
            
            if (cache.size()>10000) {
                String idToremove = cache.keySet().iterator().next();
                cache.remove(idToremove);
            }
            cache.putIfAbsent(id, file);
        
        
        }
        return file;
        
    }

    @Override
    public AuthorizationCodeResourceDetails getClient() {
        return this.client;
    }

    @Override
    public ResourceServerProperties getResource() {
        return this.resource;
    }

    @Override
    protected void consumeCredential(Credential credential) {
        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public void setClient(AuthorizationCodeResourceDetails client) {
        this.client = client;
    }

    public void setResource(ResourceServerProperties resource) {
        this.resource = resource;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    

}
