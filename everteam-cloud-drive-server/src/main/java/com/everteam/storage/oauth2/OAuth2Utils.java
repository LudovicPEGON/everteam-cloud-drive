package com.everteam.storage.oauth2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.drive.IDrive;
import com.everteam.storage.drive.OAuth2DriveImpl;
import com.everteam.storage.jackson.Encryptor;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

/**
 * Utility class for OAuth2 authentification based on google oauth2
 * <br>
 *
 * The first step is to call loadCredential(String) based on the known user ID to check if the end-user's credentials are already known.<br>
 * If not, call newAuthorizationUrl() and direct the end-user's browser to an authorization page.<br>
 * The web browser will then redirect to the redirect URL with a "code" query parameter which can then be used to request an access token using newTokenRequest(String).<br>
 * Finally, use createAndStoreCredential(TokenResponse, String) to store and obtain a credential for accessing protected resources.<br>
 *
 *
 */

@Component
@Scope("prototype")
public class OAuth2Utils {

    @Value("${server.front}")
    private String front;
    
    private final static String CALLBACK_URL = "oauth2/callback";
    private final static String CREDENTIAL_REPOSITORY = ".credentials/drive-java-etms-storage";
    private AuthorizationCodeFlow authorizationCodeFlow;
    private AuthorizationCodeResourceDetails authDetails;
    private IDrive drive = null;
    
    public OAuth2Utils() {}
    
    public void init(IDrive drive) throws GeneralSecurityException, IOException {
        Builder codeflowBuilder = null;
        
        if (drive instanceof OAuth2DriveImpl) {
            this.drive = drive;
            OAuth2DriveImpl oauth2Drive = (OAuth2DriveImpl) drive;
            authDetails = oauth2Drive.getClient();
            
            ESRepository repository = drive.getRepository();
            if (!StringUtils.isEmpty(repository.getClientId())) {
                authDetails.setClientId(repository.getClientId());
            }
            if (!StringUtils.isEmpty(repository.getClientSecret())) {
                authDetails.setClientSecret(repository.getClientSecret());
            }
            codeflowBuilder = getAuthorizationCodeFlowBuilder(drive);
            authorizationCodeFlow = codeflowBuilder.build();
        }
        else {
            throw new TypeMismatchException(drive, OAuth2DriveImpl.class);
        }
    }
    
    private AuthorizationCodeFlow.Builder getAuthorizationCodeFlowBuilder(IDrive drive) throws GeneralSecurityException, IOException {
        Builder builder = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(), GoogleNetHttpTransport.newTrustedTransport(), 
                JacksonFactory.getDefaultInstance(), new GenericUrl(authDetails.getAccessTokenUri()), new ClientParametersAuthentication(
                        authDetails.getClientId(), authDetails.getClientSecret()), authDetails.getClientId(), authDetails.getUserAuthorizationUri());
        builder.setCredentialDataStore(StoredCredential.getDefaultDataStore(new FileDataStoreFactory(new File(CREDENTIAL_REPOSITORY, drive.getRepository().getId()))));
        return builder;
    }

    public Credential loadCredential() throws IOException {
        return authorizationCodeFlow.loadCredential(authDetails.getClientId());
    }
    
    public HttpHeaders newAuthorization() throws URISyntaxException, IOException {
        AuthorizationCodeRequestUrl url = authorizationCodeFlow.newAuthorizationUrl();
        url.setScopes(authDetails.getScope());
        url.setRedirectUri(getCallbackURL());
        String state = Encryptor.encrypt(drive.getRepository().getId());
        url.setState(state);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(url.toURI());
        return headers;
    }

    private String getCallbackURL() {
        return UriBuilder.fromPath(front)
            .path(CALLBACK_URL)
            .build().toString();
    }

    public Credential createAndStoreCredential(String authorizationCode) throws IOException {
        TokenResponse tokenResponse = authorizationCodeFlow.newTokenRequest(authorizationCode).setRedirectUri(getCallbackURL()).execute();
        return authorizationCodeFlow.createAndStoreCredential(tokenResponse, authDetails.getClientId());
    }

}
