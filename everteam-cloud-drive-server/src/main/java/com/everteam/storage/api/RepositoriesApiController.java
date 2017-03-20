package com.everteam.storage.api;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.services.RepositoryService;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-09T15:45:07.545Z")

@Controller
public class RepositoriesApiController implements RepositoriesApi {
    @Autowired
    RepositoryService repositoryService;

    @Override
    public ResponseEntity<List<ESRepository>> listRepositories() {
        try {
            return new ResponseEntity<List<ESRepository>>(repositoryService.getRepositoryList(), HttpStatus.OK);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

}
