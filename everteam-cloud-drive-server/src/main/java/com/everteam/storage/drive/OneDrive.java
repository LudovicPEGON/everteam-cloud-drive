package com.everteam.storage.drive;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.onedrive.OneDriveClientAPI;
import com.everteam.storage.utils.FileInfo;
import com.google.api.client.auth.oauth2.Credential;

@Component(value="onedrive")
@Scope("prototype")
@ConfigurationProperties(prefix = "onedrive")
public class OneDrive extends OAuth2DriveImpl {

    private OneDriveClientAPI         api;
    
    protected AuthorizationCodeResourceDetails client;
    
    protected ResourceServerProperties resource;
    
    @Override
    public void init(ESRepository repository) throws IOException, GeneralSecurityException {
        super.init(repository);
    }
    
    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }
    public void setClient(AuthorizationCodeResourceDetails client) {
        this.client = client;
    }
    public ResourceServerProperties getResource() {
        return resource;
    }
    public void setResource(ResourceServerProperties resource) {
        this.resource = resource;
    }



    @Override
    public ESFileList children(String parentId, boolean addPermissions, boolean addChecksum, int maxSize) throws IOException {
        return api.children(parentId, addPermissions, maxSize);
    }

    @Override
    public void downloadTo(String fileId, OutputStream outputstream) throws IOException {
        api.downloadTo(fileId, outputstream);
    }

    @Override
    public String insertFile(String parentId, FileInfo info) throws IOException {
        return api.insertFile(parentId, info);
    }
    
    @Override
    public String insertFolder(String parentId, String name, String description) throws IOException {
        return api.insertFolder(parentId, name, description);
    }
    
    @Override
    public boolean isFolder(String fileId) throws IOException {
        return api.isFolder(fileId);
    }

    @Override
    public List<ESPermission> getPermissions(String fileId) throws IOException {
        return api.getPermissions(fileId);
    }

    @Override
    public void delete(String fileId) throws IOException {
        api.delete(fileId);
    }

    @Override
    public ESFile getFile(String fileId, boolean addPermissions,  boolean addChecksum) throws IOException {
        return api.getFile(fileId, addPermissions, addChecksum);
    }

    @Override
    public void update(String fileId, FileInfo info) throws IOException {
        api.update(fileId, info);
    }

    @Override
    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        api.checkUpdates(fileId, fromDate, consumer);

    }

    @Override
    public boolean exists(String fileId) throws IOException {
        boolean exist = false;
        try {
            getFile(fileId, false, false);
            exist = true;
        }
        catch (HttpClientErrorException e) {
            if (!HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
               throw e; 
            }
        }
        return exist;
    }

    @Override
    protected void consumeCredential(Credential credential) {
        api = new OneDriveClientAPI(credential.getAccessToken());
    }
    
}
