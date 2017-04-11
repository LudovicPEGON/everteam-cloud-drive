package com.everteam.storage.oauth2;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.everteam.storage.drive.IDrive;
import com.everteam.storage.jackson.Encryptor;
import com.everteam.storage.services.RepositoryService;
import com.google.api.client.auth.oauth2.Credential;

@Controller
public class OAuth2ApiController implements OAuth2Api {

    
    @Autowired
    RepositoryService repositoryService;
    
    @Autowired
    OAuth2Utils oauth2;
    
    
    @Override
    public ResponseEntity<String> getRepositoryTokenCallback( @RequestParam("code") String authorizationCode, @RequestParam("state") String state) {
        Credential credential = null;
        try {
            String repositoryId = Encryptor.decrypt(state);
            IDrive drive = repositoryService.getDrive(repositoryId);
            oauth2.init(drive);
            credential = oauth2.createAndStoreCredential(authorizationCode);
            return new ResponseEntity<String>(credential.getAccessToken(), HttpStatus.OK);
        } catch (IOException | GeneralSecurityException e) {
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }
        
    }
}
