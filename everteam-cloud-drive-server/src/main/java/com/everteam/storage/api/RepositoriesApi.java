package com.everteam.storage.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.everteam.storage.common.model.ESRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-04-05T09:21:35.850Z")

@Api(value = "repositories", description = "the repositories API")
public interface RepositoriesApi {

    @ApiOperation(value = "Get or Generate access token", notes = "get a valid access token or generate a new one ", response = String.class, tags={ "Administration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "valid access token", response = String.class),
        @ApiResponse(code = 303, message = "need to validate again permissions, so redirected to drive specific authentication page", response = String.class),
        @ApiResponse(code = 500, message = "if error happend on server side", response = String.class) })
    @RequestMapping(value = "/repositories/{id}/oauth2/token",
        produces = { "text/plain" }, 
        method = RequestMethod.GET)
    default ResponseEntity<String> getRepositoryToken(@ApiParam(value = "Repository Id",required=true ) @PathVariable("id") String id) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @ApiOperation(value = "Callback called only by Drive authentication server", notes = "<b> Don't called this one manually.</b> It's only callback called by Drive authentication server.  ", response = String.class, tags={ "Administration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "valid access token", response = String.class),
        @ApiResponse(code = 500, message = "if error happend on server side", response = String.class) })
    @RequestMapping(value = "/repositories/oauth2/callback",
        produces = { "text/plain" }, 
        method = RequestMethod.GET)
    default ResponseEntity<String> getRepositoryTokenCallback(@ApiParam(value = "authorization code returned by drive authentication server") @RequestParam(value = "authorizationCode", required = false) String authorizationCode,
        @ApiParam(value = "state returned by drive authentication server wich contains repositoryId") @RequestParam(value = "state", required = false) String state) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
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
