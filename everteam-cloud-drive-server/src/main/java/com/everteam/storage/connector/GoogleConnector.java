package com.everteam.storage.connector;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;

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
    public void insert(ESFile file, InputStream in) {
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
    
    
    
}
