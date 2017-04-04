package com.everteam.storage.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.utils.ESFileId;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-27T09:13:00.959Z")

@Api(value = "repositories", description = "the repositories API")
public interface RepositoriesApi {

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
    
    @ApiOperation(value = "Generate token", notes = "", response = String.class, tags={ "Repositories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "generate token", response = String.class) })
    @RequestMapping(value = "/repositories/{id}/oauth2/token",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<String> getRepositoryToken(@ApiParam(value = "id",required=true ) @PathVariable("id") ESFileId id) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    
    @ApiOperation(value = "Generate token", notes = "", response = String.class, tags={ "Repositories", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "generate token", response = String.class) })
    @RequestMapping(value = "/repositories/oauth2/callback",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<String> getRepositoryTokenCallback(@ApiParam(value = "Authorization code") @RequestParam("code") String authorizationCode,
            @ApiParam(value = "State") @RequestParam("state") String state) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
}
