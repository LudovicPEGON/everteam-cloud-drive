package com.everteam.storage.api;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.services.FileService;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-10T14:12:04.281Z")

@Controller
public class FilesApiController implements FilesApi {

    @Autowired
    FileService fileService;

    @Override
    public ResponseEntity<Void> checkUpdates(@PathVariable("id") ESFileId fileId,
            @RequestParam(value = "fromDate", required = true) OffsetDateTime fromDate) {
        try {
            fileService.checkUpdates(fileId, fromDate);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }
    

    @Override
    public ResponseEntity<ESFile> copyFile(@PathVariable("id") ESFileId fileId,
            @RequestParam(value = "targetId", required = true) ESFileId targetId) {
        try {
            ESFileId result = fileService.copy(fileId, targetId);

            return new ResponseEntity<ESFile>(fileService.getFile(result, false, false), HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ESFile> createFile(@PathVariable("id") ESFileId fileId,
            @RequestParam("content") MultipartFile content, @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description) {
        try {
            if (name == null || name.length() == 0) {
                name = content.getName();
            }
            ESFileId newFileId = fileService.create(fileId, name, content.getContentType(), content.getInputStream(), description);
            return new ResponseEntity<ESFile>(fileService.getFile(newFileId, false, false), HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> deleteFile(@PathVariable("id") ESFileId fileId) {
        try {
            fileService.delete(fileId);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } catch (WebApplicationException | IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ESFileList> getFileChildren(@PathVariable("id") ESFileId fileId,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum,
            @RequestParam(value = "maxResult", required = false, defaultValue = "100") Integer maxResult) {
        try {
            if (maxResult<0) {
                maxResult = Integer.MAX_VALUE;
            }
            ESFileList efl = fileService.getChildren(fileId, getPermissions, getChecksum, maxResult);
            return new ResponseEntity<ESFileList>(efl, HttpStatus.OK);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> getFileContent(@PathVariable("id") ESFileId fileId) {

        try {
            ESFile file = fileService.getFile(fileId, false, false);
            byte[] data = fileService.getFileContent(fileId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", file.getMimeType());

            headers.add("content-disposition", "attachment; filename = " + file.getName());
            return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ESFile> getFile(@PathVariable("id") ESFileId fileId,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum) {
        try {
            ESFile file = fileService.getFile(fileId, getPermissions, getChecksum);
            return new ResponseEntity<ESFile>(file, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ESPermission>> getFilePermissions(@PathVariable("id") ESFileId fileId) {
        try {
            List<ESPermission> permissions = fileService.getPermissions(fileId);
            return new ResponseEntity<List<ESPermission>>(permissions, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

    }

    
    
    @Override
    public ResponseEntity<ESFile> moveFile(@PathVariable("id") ESFileId fileId,
        @RequestParam(value = "targetId", required = true) ESFileId targetId) {
        try {
            ESFileId copiedFileId = fileService.copy(fileId, targetId);
            fileService.delete(fileId);
            ESFile copiedFile = fileService.getFile(copiedFileId, false, false);
            return new ResponseEntity<ESFile>(copiedFile, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
    
    
    @Override
    public ResponseEntity<ESFile> updateFile(@PathVariable("id") ESFileId fileId, 
            @RequestParam("content") MultipartFile content,
            @RequestParam(value = "description", required = false) String description) {
        try {
            fileService.update(fileId, content.getName(), content.getContentType(), content.getInputStream(),  description);
            ESFile updatedFile = fileService.getFile(fileId, false, false);
            return new ResponseEntity<ESFile>(updatedFile, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
    
   

}
