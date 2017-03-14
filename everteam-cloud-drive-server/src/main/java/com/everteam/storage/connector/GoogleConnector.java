package com.everteam.storage.connector;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.managers.FileId;

@Component
@Scope("prototype")
public class GoogleConnector extends ConnectorImpl {
    @Value("${storage.google.test:#{1000}}")
    private int test;

   

    @Override
    public ESFileList children(String parentId) {
        // TODO Auto-generated method stub
        return null;
    }





    @Override
    public FileId insert(ESFile file, InputStream in) {
        return null;
        // TODO Auto-generated method stub
        
    }



    @Override
    public void downloadTo(String fileId, OutputStream outputstream) {
        // TODO Auto-generated method stub
        
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





    @Override
    public void update(FileId fileId, MultipartFile content, String name, String description) {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
