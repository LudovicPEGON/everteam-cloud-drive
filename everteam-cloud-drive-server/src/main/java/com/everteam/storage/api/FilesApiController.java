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
import com.everteam.storage.common.model.ESParent;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.serializers.Encryptor;
import com.everteam.storage.managers.FileId;
import com.everteam.storage.managers.FilesManager;

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
            return new ResponseEntity<ESFile>(file, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        
    }

    @Override
    public ResponseEntity<ESFile> createFile(@PathVariable("id") String id,
            @RequestPart("content") MultipartFile content,
            @RequestPart(value="name", required=true)  String name,
            @RequestPart(value="description", required=false)  String description) {
        try {
            ESFile file = new ESFile()
                    .name(name)
                    .addParentsItem(new ESParent().id(getDecyptId(id)))
                    .description(description)
                    .mimeType(content.getContentType());
            FileId newFile = filesManager.create(file, content.getInputStream());
            return new ResponseEntity<ESFile>(filesManager.getFile(newFile), HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Void> deleteFile(@PathVariable("id") String id) {
        try {
            filesManager.delete(getDecyptId(id));
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } catch (WebApplicationException | IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ESFileList> getFileChildren(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "maxResult", required = false, defaultValue="100") Integer maxResult) {
        try {
            ESFileList efl = filesManager.getChildren(getDecyptId(id));
            return new ResponseEntity<ESFileList>(efl, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> getFileContent(@PathVariable("id") String id) {
        ESFile file = buildFile(id);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=" + file.getName());
        responseHeaders.add("Content-Type",file.getMimeType());

        try {
            return new ResponseEntity<byte[]>(filesManager.getFileContent(getDecyptId(id)), responseHeaders, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ESFile> getFile(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions) {
        ESFile file = buildFile(id);
        return new ResponseEntity<ESFile>(file,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ESPermission>> getFilePermissions(@PathVariable("id") String id) {
        id = getDecyptId(id);
        List<ESPermission> permissions = filesManager.getPermissions(id);

        return new ResponseEntity<List<ESPermission>>(permissions,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ESFile> updateFile(@PathVariable("id") String id,
            @RequestPart("file") MultipartFile content,
            @RequestPart(value="name", required=false)  String name,
            @RequestPart(value="description", required=false)  String description) {
        try {
            String decryptedId = getDecyptId(id);
            filesManager.update(decryptedId, content, name, description);
            ESFile updatedFile = filesManager.getFile(FileId.get(decryptedId));
            return new ResponseEntity<ESFile>(updatedFile, HttpStatus.OK);
        } catch (WebApplicationException | IOException e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

    private ESFile buildFile(String id) {
        FileId fileId = FileId.get(getDecyptId(id));
        return filesManager.getFile(fileId);
    }

    private String getDecyptId(String id) throws WebApplicationException {
        try {
            return Encryptor.decrypt(id);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

}
