package com.everteam.storage.api;

import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESRepository;
import java.time.OffsetDateTime;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-14T08:27:34.208Z")

@Api(value = "repositories", description = "the repositories API")
public interface RepositoriesApi {

    @ApiOperation(value = "Search updated files.", notes = "Launch a batch which is going to search files which have been updated after a specified date. ", response = Void.class, tags={ "Repositories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successfully started", response = Void.class) })
    @RequestMapping(value = "/repositories/{id}/checkUpdates",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<Void> checkUpdates(@ApiParam(value = "Repository Id",required=true ) @PathVariable("id") String id,
        @ApiParam(value = "Return only files modified after this date") @RequestParam(value = "afterModifiedDate", required = false) OffsetDateTime afterModifiedDate) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiOperation(value = "Get repository children.", notes = "return repository direct children. A child can be a directory or a file. ", response = ESFileList.class, tags={ "Repositories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = ESFileList.class) })
    @RequestMapping(value = "/repositories/{id}/children",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<ESFileList> getRepositoryChildren(@ApiParam(value = "Repository Id",required=true ) @PathVariable("id") String id,
        @ApiParam(value = "set true if you want to get permissions at the same you get files", defaultValue = "false") @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions,
        @ApiParam(value = "Maximum number of files to return. Acceptable values are 0 to 1000. Use -1 for no limit.", defaultValue = "100") @RequestParam(value = "maxResult", required = false, defaultValue="100") Integer maxResult) {
        // do some magic!
        return new ResponseEntity<ESFileList>(HttpStatus.OK);
    }


    @ApiOperation(value = "Search repositories", notes = "you can search for available repositories in the storage service ", response = ESRepository.class, responseContainer = "List", tags={ "Repositories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = ESRepository.class) })
    @RequestMapping(value = "/repositories",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<List<ESRepository>> listRepositories() {
        // do some magic!
        return new ResponseEntity<List<ESRepository>>(HttpStatus.OK);
    }

}
