package com.everteam.storage.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.everteam.storage.common.model.ESRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-24T10:07:37.096Z")

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

}
