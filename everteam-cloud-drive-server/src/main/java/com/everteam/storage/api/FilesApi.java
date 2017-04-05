package com.everteam.storage.api;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.utils.ESFileId;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:13:00.959Z")

@Api(value = "files", description = "the files API")
public interface FilesApi {

    @ApiOperation(value = "Search updated files.", notes = "Launch a batch which is going to search files which have been updated after a specified date. ", response = Void.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successfully started", response = Void.class) })
    @RequestMapping(value = "/files/{id}/checkUpdates",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<Void> checkUpdates(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "Return only files modified after this date", required = true) @RequestParam(value = "fromDate", required = true) OffsetDateTime fromDate) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Create a copy", notes = "Copy a file of the specified file", response = ESFile.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created", response = ESFile.class) })
    @RequestMapping(value = "/files/{id}/copy",
        method = RequestMethod.POST)
    default ResponseEntity<ESFile> copyFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "Target file's ID", required = true) @RequestParam(value = "targetId", required = true) ESFileId targetId) {
        // do some magic!
        return new ResponseEntity<ESFile>(HttpStatus.OK);
    }


    @ApiOperation(value = "Insert a new File", notes = "Create a new File using file content and required parameters", response = ESFile.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "If successful, this method returns the created File in the response body", response = ESFile.class) })
    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    default ResponseEntity<ESFile> createFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "file detail") @RequestPart("file") MultipartFile content,
        @ApiParam(value = "if content exists, we takes file name by default") @RequestParam(value = "name", required = false) String name,
        @ApiParam(value = "") @RequestParam(value = "description", required = false) String description) {
        // do some magic!
        return new ResponseEntity<ESFile>(HttpStatus.OK);
    }


    @ApiOperation(value = "Delete a File", notes = "Delete a File or a directory using its id", response = Void.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class) })
    @RequestMapping(value = "/files/{id}",
        method = RequestMethod.DELETE)
    default ResponseEntity<Void> deleteFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get file informations", notes = "Get file informations", response = ESFile.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results", response = ESFile.class) })
    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<ESFile> getFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "set true if you want to get permissions at the same time you get files", defaultValue = "false") @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions,
        @ApiParam(value = "set true if you want to get checksum at the same time you get files", defaultValue = "false") @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum) {
        // do some magic!
        return new ResponseEntity<ESFile>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get the file children", notes = "Get the specified file's direct children", response = ESFileList.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = ESFileList.class) })
    @RequestMapping(value = "/files/{id}/children",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<ESFileList> getFileChildren(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "set true if you want to get permissions at the same time you get files", defaultValue = "false") @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions,
        @ApiParam(value = "set true if you want to get checksum at the same time you get files", defaultValue = "false") @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum,
        @ApiParam(value = "Maximum number of files to return. Acceptable values are 0 to 1000. Use -1 for no limit", defaultValue = "100") @RequestParam(value = "maxResult", required = false, defaultValue="100") Integer maxResult) {
        // do some magic!
        return new ResponseEntity<ESFileList>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get the content file", notes = "Get the specified file's content", response = byte[].class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully retrieved", response = byte[].class) })
    @RequestMapping(value = "/files/{id}/content",
        produces = { "application/octet-stream" }, 
        method = RequestMethod.GET)
    default ResponseEntity<byte[]> getFileContent(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id) {
        // do some magic!
        return new ResponseEntity<byte[]>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get File's Permsissions", notes = "Lists a file's permissions", response = ESPermission.class, responseContainer = "List", tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = ESPermission.class) })
    @RequestMapping(value = "/files/{id}/permissions",
        method = RequestMethod.GET)
    default ResponseEntity<List<ESPermission>> getFilePermissions(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id) {
        // do some magic!
        return new ResponseEntity<List<ESPermission>>(HttpStatus.OK);
    }


    @ApiOperation(value = "Move a file", notes = "Move a file to a specified parent", response = ESFile.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully moved", response = ESFile.class) })
    @RequestMapping(value = "/files/{id}/move",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<ESFile> moveFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "Target file's ID", required = true) @RequestParam(value = "targetId", required = true) ESFileId targetId) {
        // do some magic!
        return new ResponseEntity<ESFile>(HttpStatus.OK);
    }


    @ApiOperation(value = "Updates a file", notes = "Updates a specified file metadata and/or content", response = ESFile.class, tags={ "Files", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "If successful, this method returns the updated File in the response body", response = ESFile.class) })
    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.PUT)
    default ResponseEntity<ESFile> updateFile(@ApiParam(value = "The file's ID",required=true ) @PathVariable("id") ESFileId id,
        @ApiParam(value = "file detail") @RequestPart("file") MultipartFile content,
        @ApiParam(value = "") @RequestParam(value = "description", required = false) String description) {
        // do some magic!
        return new ResponseEntity<ESFile>(HttpStatus.OK);
    }

}
