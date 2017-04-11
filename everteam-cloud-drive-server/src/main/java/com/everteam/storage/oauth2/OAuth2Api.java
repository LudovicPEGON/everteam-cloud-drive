package com.everteam.storage.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface OAuth2Api {

    
    @RequestMapping(value = "/oauth2/callback",
        produces = { "text/plain" }, 
        method = RequestMethod.GET)
    default ResponseEntity<String> getRepositoryTokenCallback(@RequestParam(value = "authorizationCode", required = false) String authorizationCode,
        @RequestParam(value = "state", required = false) String state) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
