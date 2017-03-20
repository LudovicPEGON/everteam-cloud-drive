package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;

@Component
@Scope("prototype")
public class GoogleDrive extends DriveImpl {
    @Value("${storage.google.test:#{1000}}")
    private int lpe;

   

    @Override
    public ESFileList children(ESFileId parentId, boolean addPermissions, int maxSize ) {
        // TODO Auto-generated method stub
        return null;
    }





  



    @Override
    public void downloadTo(ESFileId fileId, OutputStream outputstream) {
        // TODO Auto-generated method stub
        
    }





    @Override
    public List<ESPermission> getPermissions(ESFileId fileId) {
        // TODO Auto-generated method stub
        return null;
    }





    @Override
    public void delete(ESFileId fileId) {
        // TODO Auto-generated method stub
        
    }





    @Override
    public ESFile getFile(ESFileId fileId, boolean addPermissions) {
        // TODO Auto-generated method stub
        return null;
    }





    @Override
    public void update(ESFileId fileId, InputStream content, String description) {
     
        
    }





    @Override
    public ESFileId insert(ESFileId fileId, InputStream in, String name, String description) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }









    @Override
    public void checkUpdates(ESFileId fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
