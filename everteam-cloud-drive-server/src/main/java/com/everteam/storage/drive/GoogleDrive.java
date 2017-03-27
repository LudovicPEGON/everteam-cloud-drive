package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESPermission.AccountTypeEnum;
import com.everteam.storage.common.model.ESPermission.RolesEnum;
import com.everteam.storage.common.model.ESPermission.TypeEnum;
import com.everteam.storage.common.model.ESUser;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.drive.model.User;

@Component(value = "google")
@Scope("prototype")
public class GoogleDrive extends DriveImpl {

    private final static Logger LOG = LoggerFactory.getLogger(GoogleDrive.class);
    
    private final ConcurrentHashMap<String, File> cache = new ConcurrentHashMap<>();

    @Value("${storage.google.test:#{1000}}")
    private int lpe2;

    /** Application name. */
    private static final String APPLICATION_NAME = "Everteam MS Storage";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
            ".credentials/drive-java-etms-storage");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);

        }
    }

    @Override
    public ESFileList children(String parentId, boolean addPermissions, boolean addChecksum, int maxSize) throws IOException {
        ESFileList result = new ESFileList();
        Drive service = getDriveService();

        Files.List request = service.files().list().setPageSize(maxSize)
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
            result.addItemsItem(buildFile(f, addPermissions,addChecksum));
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
        Files.Get request = getDriveService().files().get(fileId);
        request.setFields("*");
        return buildFile(request.execute(), addPermissions, addChecksum);
    }

    @Override
    public void update(String fileId, String name, String contentType, InputStream in, String description) throws IOException {
            // First retrieve the file from the API.
            File file = new File();
            
            // File's new metadata.
            //file.setName(newName);
            file.setDescription(description);
            //file.setMimeType(newMimeType);

            
            InputStreamContent mediaContent = new InputStreamContent(contentType, in); 

            // Send the request to the API.
            getDriveService().files().update(fileId, file, mediaContent).execute();
    }

    @Override
    public String insert(String parentId, String name, String contentType, InputStream in, String description) throws IOException {
            // File's metadata.
            File body = new File();
            body.setName(name);
            body.setDescription(description);
            body.setMimeType(contentType);

            // Set the parent folder.
            String pId = parentId;
            if (pId != null && pId.length() > 0) {
              body.setParents(Arrays.asList(pId));
            }
           

            // File's content.
            InputStreamContent mediaContent = new InputStreamContent(contentType, in); 
            File file = getDriveService().files().create(body, mediaContent).execute();
            
            
            return file.getId();
    }

    
    
    
    
    @Override
    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        Files.List request = getDriveService().files()
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
                          consumer.accept(buildFile(t, true,true));
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
     * Creates an authorized Credential object.
     * 
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GoogleDrive.class.getResourceAsStream(repository.getClientSecrets());
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline")
                        .setApprovalPrompt("auto").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        LOG.debug("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * 
     * @return an authorized Drive client service
     * @throws IOException
     */
    private Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    
    
    
    
    
    
    

    private ESFile buildFile(File file, boolean addPermissions, boolean addChecksum) throws IOException {
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
                        .paths(absPath(file)));
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
    
    
    
    
    private List<String> absPath(File file) throws IOException {
        List<String> path = new ArrayList<>();

        while (true) {
            File parent = getFile(file.getParents().get(0));

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

    private File getFile(String id) throws IOException {
        // Fetch file from drive
        File file = cache.get(id);
        if (file == null) {
            Files.Get request = getDriveService().files().get(id);
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

    
    
    
    
    
    
    
    
    
    
    
    

}
