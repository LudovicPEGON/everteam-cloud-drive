package com.everteam.storage.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
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

    @Override
    public ResponseEntity<OAuth2AccessToken> getRepositoryToken() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Map<String, String> requestParameters = new HashMap<>();
        String clientId = "acme";
        boolean approved = true;
        Set<String> scope = new HashSet<>();
        scope.add("scope");
        Set<String> resourceIds = new HashSet<>();
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add("code");
        Map<String, Serializable> extensionProperties = new HashMap<>();

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId,
                authorities, approved, scope,
                resourceIds, null, responseTypes, extensionProperties);


        User userPrincipal = new User(principal.getName(), "", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
        OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
        DefaultTokenServices dts = new DefaultTokenServices();
        OAuth2AccessToken token = dts.createAccessToken(auth);
        return new ResponseEntity<OAuth2AccessToken>(token, HttpStatus.OK);
    }
    
    

}
