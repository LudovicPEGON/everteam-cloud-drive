package com.everteam.storage.drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import com.everteam.storage.utils.OAuth2Utils;
import com.google.api.client.auth.oauth2.Credential;

public abstract class OAuth2DriveImpl extends DriveImpl {
    
    public abstract AuthorizationCodeResourceDetails getClient();
    public abstract ResourceServerProperties getResource();
    protected abstract void consumeCredential(Credential credential);
    
    public final void authorize() throws GeneralSecurityException, IOException {
        OAuth2Utils oauth2 = new OAuth2Utils(this);
        Credential credential = oauth2.loadCredential();
        if ( ( credential != null && credential.getAccessToken()!=null && credential.getExpiresInSeconds()>0 ) 
                || ( credential != null && credential.refreshToken() ) ) {
            consumeCredential(credential);
        }
        else {
            throw new CredentialsExpiredException(messages.get("error.credentials.expired"));
        }
    }
    
    
}
