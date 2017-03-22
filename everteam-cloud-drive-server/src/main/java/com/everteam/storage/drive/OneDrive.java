package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.onedrive.OneDriveClientAPI;

@Component
@Scope("prototype")
public class OneDrive extends DriveImpl {

    private final static Logger LOG = LoggerFactory.getLogger(OneDrive.class);

    private final static String RESOURCE_URL = "https://everteamsoftware365-my.sharepoint.com/personal/k_bennat_everteamsoftware365_onmicrosoft_com";
    private final static String ACCESS_TOKEN = "";
    private OneDriveClientAPI         api;

    @Override
    public void init(ESRepository repository) {
        super.init(repository);
//        api = new OneDriveBusinessAPI(RESOURCE_URL, ACCESS_TOKEN);
//        api = new OneDriveBasicAPI(ACCESS_TOKEN);
         api = new OneDriveClientAPI(ACCESS_TOKEN);
    }

    @Override
    public ESFileList children(ESFileId parentId, boolean addPermissions, int maxSize) throws IOException {
//        OneDriveFolder parent = getOneDriveFolder(parentId);
//        ESFileList files = new ESFileList();
//        for (OneDriveItem.Metadata metadata : parent.getChildren()) {
//            files.addItemsItem(getESFile(metadata));
//        }
        return api.children(parentId, addPermissions, maxSize);
    }

    @Override
    public void downloadTo(ESFileId fileId, OutputStream outputstream) throws IOException {
//        OneDriveFile file = getOneDriveFile(fileId);
//        try (InputStream is = file.download()) {
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = is.read(buffer)) > 0) {
//                outputstream.write(buffer, 0, length);
//            }
//        }
    }

    @Override
    public ESFileId insert(ESFileId parentId, InputStream in, String name, String description) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ESPermission> getPermissions(ESFileId fileId) throws IOException {
        return api.getPermissions(fileId);
    }

    @Override
    public void delete(ESFileId fileId) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public ESFile getFile(ESFileId fileId, boolean addPermissions) throws IOException {
//        OneDriveItem item = getOneDriveItem(fileId);
//        ESFile file = getESFile(item.getMetadata());
        return api.getFile(fileId, addPermissions);
    }

    @Override
    public void update(ESFileId fileId, InputStream in, String description) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkUpdates(ESFileId fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        // TODO Auto-generated method stub

    }
    
//    private OneDriveItem getOneDriveItem(ESFileId fileId) throws OneDriveAPIException {
//        OneDriveItem item = null;
//        try {
//            item = getOneDriveFolder(fileId);
//        }
//        catch (OneDriveAPIException e) {
//            item = getOneDriveFile(fileId);
//        }
//        return item;
//    }
//    
//    private OneDriveFile getOneDriveFile(ESFileId fileId) throws OneDriveAPIException {
//        OneDriveFile file = null;
//        file = new OneDriveFile(api, fileId.getRelativeId());
//        // just to make sure it's a folder
//        ((OneDriveFile)file).getMetadata();
//        return file;
//    }
//
//    private OneDriveFolder getOneDriveFolder(ESFileId fileId) throws OneDriveAPIException {
//        OneDriveFolder folder = null;
//        if (fileId!=null && !fileId.getRelativeId().isEmpty()) {
//            folder = new OneDriveFolder(api, fileId.getRelativeId());
//            // just to make sure it's a folder
//            ((OneDriveFolder)folder).getMetadata();
//        }
//        else {
//            folder = OneDriveFolder.getRoot(api);
//        }
//        return folder;
//    }
//    
//    private ESFile getESFile(Metadata metadata) {
//        ESFile file = new ESFile()
//                .id(getESFileId(this, metadata.getId()))
//                .name(metadata.getName())
//                .description(metadata.getDescription())
//                .creationTime(metadata.getCreatedDateTime().toOffsetDateTime())
//                .directory(metadata.isFolder())
//                .fileSize(metadata.getSize())
//                .lastAccessTime(metadata.getLastModifiedDateTime().toOffsetDateTime())
//                .lastModifiedTime(metadata.getLastModifiedDateTime().toOffsetDateTime())
//                .lastModifiedUser(getESUser(metadata.getLastModifiedBy().getUser()))
////                .checksum(checksum)
////                .owners()
////                .permissions(permissions)
//                ;
//        if (metadata.getParentReference()!=null) {
//            file.addParentsItem(getESParent(metadata.getParentReference()));
//        }
//        if (metadata.isFile()) {
//            file.mimeType(metadata.asFile().getMimeType());
//        }
//        return null;
//    }
//
//    private ESParent getESParent(Reference parentReference) {
//        ESParent parent = new ESParent()
//                .id(getESFileId(this, parentReference.getId()))
//                .addPathsItem(parentReference.getPath());
//        return parent;
//    }
//
//    private ESFileId getESFileId(OneDrive oneDrive, String driveId) {
//        ESFileId fileId = new ESFileId()
//                .repositoryName(this.getRepository().getName())
//                .relativeId(driveId);
//        return fileId;
//    }
//
//    private ESUser getESUser(OneDriveIdentity odi) {
//        ESUser user = new ESUser()
//                .displayName(odi.getDisplayName())
//                .id(odi.getId());
//        return user;
//    }
//    
//    
//    protected class ESOneDriveAPI extends OneDriveBasicAPI {
//
//        public ESOneDriveAPI(String accessToken) {
//            super(accessToken);
//        }
//
//        @Override
//        public String getBaseURL() {
//            return "https://graph.microsoft.com/v1.0";
//        }
//        
//        
//    }

}
