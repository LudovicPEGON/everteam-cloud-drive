package com.everteam.storage.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.serializers.Encryptor;
import com.everteam.storage.managers.FilesManager;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-10T14:12:04.281Z")

@Controller
public class FilesApiController implements FilesApi {

    @Autowired
    FilesManager filesManager;

    @Override
    public ResponseEntity<ESFile> copyFile(@PathVariable("id") String id, @RequestBody ESFile file) {
        ESFile fsource = buildFile(id);
        try {
            filesManager.copy(fsource, file);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ESFile>(file, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ESFile> createFile(@PathVariable("id") String id,
            @RequestPart("file") MultipartFile content,
            @RequestPart(value="name", required=true)  String name,
            @RequestPart(value="description", required=false)  String description) {
        try {
            ESFile file = null;
            filesManager.create(file, content.getInputStream());
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
        return FilesApi.super.createFile(id, content, name, description);
    }

    @Override
    public ResponseEntity<Void> deleteFile(String id) {
        filesManager.delete(getDecyptId(id));

        return FilesApi.super.deleteFile(id);
    }

    @Override
    public ResponseEntity<ESFileList> getFileChildren(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "maxResult", required = false, defaultValue="100") Integer maxResult) {
        try {
            filesManager.getChildren(id);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        return FilesApi.super.getFileChildren(id, getPermissions, maxResult);
    }

    @Override
    public ResponseEntity<byte[]> getFileContent(@PathVariable("id") String id) {
        ESFile file = buildFile(id);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=" + file.getName());
        responseHeaders.add("Content-Type",file.getMimeType());

        try {
            return new ResponseEntity(filesManager.getFileContent(id), responseHeaders,HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ESFile> getFile(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions) {
        ESFile file = buildFile(id);

        return FilesApi.super.getFile(id, getPermissions);
    }

    @Override
    public ResponseEntity<List<ESPermission>> getFilePermissions(@PathVariable("id") String id) {
        id = getDecyptId(id);
        List<ESPermission> permissions = filesManager.getPermissions(id);

        return FilesApi.super.getFilePermissions(id);
    }

    @Override
    public ResponseEntity<ESFile> updateFile(@PathVariable("id") String id,
            @RequestPart("file") MultipartFile content,
            @RequestPart(value="name", required=false)  String name,
            @RequestPart(value="description", required=false)  String description) {
    
        id = getDecyptId(id);

        filesManager.update(id, content, name, description);

        return FilesApi.super.updateFile(id, content, name, description);
    }

    private ESFile buildFile(String id) {
        return filesManager.getFile(getDecyptId(id));
    }

    private String getDecyptId(String id) throws WebApplicationException {
        try {
            return Encryptor.decrypt(id);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

}
