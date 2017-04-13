package com.everteam.storage.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.converters.FileIdConverter;
import com.everteam.storage.services.FileService;
import com.everteam.storage.utils.ESFileId;
import com.everteam.storage.utils.FileInfo;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-10T14:12:04.281Z")

@Controller
public class FilesApiController implements FilesApi {

    @Autowired
    FileService fileService;

    @Override
    public ResponseEntity<Void> checkUpdates(@PathVariable("id") String encryptedFileId,
            @RequestParam(value = "fromDate", required = true) OffsetDateTime fromDate) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            fileService.checkUpdates(fileId  , fromDate);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }
    

    @Override
    public ResponseEntity<ESFile> copyFile(@PathVariable("id") String encryptedFileId,
            @RequestParam(value = "targetId", required = true) String encryptedTargetId) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFileId targetId = new FileIdConverter().convert(encryptedTargetId);
            ESFileId result = fileService.copy(fileId, targetId);

            return new ResponseEntity<ESFile>(fileService.getFile(result, false, false), HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ESFile> createFile(@PathVariable("id") String encryptedFileId,
            @RequestParam(value = "file", required = false) MultipartFile content, @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFileId newFileId = null;
            // if content is null we're trying to insert folder
            if (content == null) {
                if (name != null && !name.isEmpty()) {
                    newFileId = fileService.createFolder(fileId, name, description);
                }
                else {
                    throw new WebApplicationException(Status.BAD_REQUEST);
                }
            }
            // else we're trying to insert a file
            else {
                if (name == null || name.length() == 0) {
                    name = content.getName();
                }
                FileInfo info = new FileInfo(name, description, content.getContentType(), content.getSize(), content.getInputStream());
                newFileId = fileService.createFile(fileId, info);
            }
            return new ResponseEntity<ESFile>(fileService.getFile(newFileId, false, false), HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> deleteFile(@PathVariable("id") String encryptedFileId) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            fileService.delete(fileId);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } catch (WebApplicationException | IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ESFileList> getFileChildren(@PathVariable("id") String encryptedFileId,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum,
            @RequestParam(value = "maxResult", required = false, defaultValue = "100") Integer maxResult) {
        try {
            if (maxResult<0) {
                maxResult = Integer.MAX_VALUE;
            }
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFileList efl = fileService.getChildren(fileId, getPermissions, getChecksum, maxResult);
            return new ResponseEntity<ESFileList>(efl, HttpStatus.OK);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> getFileContent(@PathVariable("id") String encryptedFileId) {

        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFile file = fileService.getFile(fileId, false, false);
            byte[] data = fileService.getFileContent(fileId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", file.getMimeType());

            headers.add("content-disposition", "attachment; filename = " + file.getName());
            return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ESFile> getFile(@PathVariable("id") String encryptedFileId,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFile file = fileService.getFile(fileId, getPermissions, getChecksum);
            return new ResponseEntity<ESFile>(file, HttpStatus.OK);
        }
        catch (FileNotFoundException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        } 
        catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ESPermission>> getFilePermissions(@PathVariable("id") String encryptedFileId) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            List<ESPermission> permissions = fileService.getPermissions(fileId);
            return new ResponseEntity<List<ESPermission>>(permissions, HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

    }

    
    
    @Override
    public ResponseEntity<ESFile> moveFile(@PathVariable("id") String encryptedFileId,
        @RequestParam(value = "targetId", required = true) String encryptedTargetId) {
        try {
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            ESFileId targetId = new FileIdConverter().convert(encryptedTargetId);
            ESFileId copiedFileId = fileService.copy(fileId, targetId);
            fileService.delete(fileId);
            ESFile copiedFile = fileService.getFile(copiedFileId, false, false);
            return new ResponseEntity<ESFile>(copiedFile, HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
    
    
    @Override
    public ResponseEntity<ESFile> updateFile(@PathVariable("id") String encryptedFileId, 
            @RequestParam("content") MultipartFile content,
            @RequestParam(value = "description", required = false) String description) {
        try {
            if (content==null) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
            ESFileId fileId = new FileIdConverter().convert(encryptedFileId);
            FileInfo info = new FileInfo(content.getName(), description, content.getContentType(), content.getSize(), content.getInputStream());
            fileService.update(fileId, info);
            ESFile updatedFile = fileService.getFile(fileId, false, false);
            return new ResponseEntity<ESFile>(updatedFile, HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
    
   

}
