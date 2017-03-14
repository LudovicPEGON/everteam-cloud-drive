package com.everteam.storage.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.common.serializers.Encryptor;
import com.everteam.storage.managers.ConnectorsManager;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-03-09T15:45:07.545Z")

@Controller
public class RepositoriesApiController implements RepositoriesApi {
    @Autowired
    ConnectorsManager connectorManager;

    
    @Override
    public ResponseEntity<ESFileList> getRepositoryChildren(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions,
            @RequestParam(value = "maxResult", required = false, defaultValue = "100") Integer maxResult
            ) {

        id = getDecyptId(id);
        try {
            ESFileList files = connectorManager.getFiles(id, getPermissions, maxResult);
            return new ResponseEntity<ESFileList>(files, HttpStatus.OK);
        } catch (IOException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ESRepository>> listRepositories() {
        return new ResponseEntity<List<ESRepository>>(connectorManager.getRepositoryList(), HttpStatus.OK);
    }

    private String getDecyptId(String id) throws WebApplicationException {
        try {
            return Encryptor.decrypt(id);
        } catch (Exception e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }

}
