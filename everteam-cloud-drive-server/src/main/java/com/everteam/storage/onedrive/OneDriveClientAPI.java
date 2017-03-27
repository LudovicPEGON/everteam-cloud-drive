package com.everteam.storage.onedrive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESPermission.AccountTypeEnum;
import com.everteam.storage.common.model.ESPermission.RolesEnum;
import com.everteam.storage.common.model.ESPermission.TypeEnum;
import com.everteam.storage.common.model.ESUser;
import com.everteam.storage.drive.OneDrive;

public class OneDriveClientAPI {

    private final static Logger LOG = LoggerFactory.getLogger(OneDrive.class);
    
    private final String ACCESS_TOKEN;
    private final String BASE_URL = "https://graph.microsoft.com/v1.0/me";
    
    public OneDriveClientAPI(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }
    /*
     * Source https://dev.onedrive.com/resources/item.htm
     */
    private static final String ITEM_CHILDREN_URL = "/drive/items/{item-id}/children";
    private static final String ROOT_CHILDREN_URL = "/drive/root/children";
    private static final String ITEM_PERMISSIONS_URL = "/drive/items/{item-id}/permissions";
    
    private static final String ITEM_URL = "/drive/items/{id}";
    
    @SuppressWarnings("unused")
    private static final String ITEM_CONTENT_URL = "/drive/items/{item-id}/content";
    
    public static void main(String [] args) {
        OneDriveClientAPI oda = new OneDriveClientAPI("eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKcHY0UnRMdjNKZDZ4T0oxTXBIdWlwdlJvbGw0WklFMG1sdUVTX291ZDlRX2NNN3JnakpoNklFUHc2bnhMempiRFVFTjBCZUVfNXVfbW8yTUh6UWN2d3lBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIiwia2lkIjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9hYmNhMGRlMy0yMGJmLTQwODEtYmRkMS1hYjY2YjA3NWMxNTQvIiwiaWF0IjoxNDkwMTg3NjkxLCJuYmYiOjE0OTAxODc2OTEsImV4cCI6MTQ5MDE5MTU5MSwiYWNyIjoiMSIsImFpbyI6IkFRQUJBQUVBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKNGowNDhYRUFQQzhjV0cycUZFcEtuaFYyWmRvU0UxWjhheGNWZXhhekRDNFJoVjI5Mlo5TEEwbVVMVGduc1RvalU4Vnl1TWFManJZbFJVZW11aWE2c3ZKMHltNVdub2twSVE2QklaQVl2ZFlnQUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Ik9uZURyaXZlU3RvcmFnZSIsImFwcGlkIjoiZTQ4ODAyODEtN2JjNi00OGM0LTlhOTUtNGIwZWU5ZmIwY2JhIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJCZW5uYXQiLCJnaXZlbl9uYW1lIjoiS2FkZXIiLCJpcGFkZHIiOiI5MC44NS4yMDMuMTc3IiwibmFtZSI6IkthZGVyIEJlbm5hdCIsIm9pZCI6IjhkZDVjNzI0LTcwMzUtNDhhNS05NDgxLTBjYTQwY2JlNzMxOCIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzN0ZGRTlGREYxQTJCIiwic2NwIjoiRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5BbGwgVXNlci5SZWFkIiwic2lnbmluX3N0YXRlIjpbImttc2kiXSwic3ViIjoiRTBBOGtLYWZ2MXFCUE51bjNCOTNJckctbXMweHFLc0EyNC02NlNzaVZlYyIsInRpZCI6ImFiY2EwZGUzLTIwYmYtNDA4MS1iZGQxLWFiNjZiMDc1YzE1NCIsInVuaXF1ZV9uYW1lIjoiay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJrLmJlbm5hdEBldmVydGVhbXNvZnR3YXJlMzY1Lm9ubWljcm9zb2Z0LmNvbSIsInZlciI6IjEuMCJ9.WVa1RTnPCRZXeWv2GCx33o-m_SKgQkIFSljxhBquL-VApF3CX_fyuxMjPPFphYx3PqbBUnx0Nfe2NSdDQBHOI-s9NQxpG5OuQRBy7W-808RKSV_pHOU0Nb8wAxhfuHGkvEwOd_NMTv9rzRDKPdrWFAUUQLMICvdPrwTHerwEcIjRVL54CdBEet7CVHjLzBUIFjqp9i2LApZCTLZ28HNqq8V29zgtwZOuF1FI918qm4b5AnfaamKVCAtUm0yeh-V_q_BIarzvJomvIBlDRIbG3cdj6KBMZTpKJDLEPe6meVU1p6h2MqUXG9SWzsrcbx5lAvQZ55QGsiF8fBuwZPlvbQ");
        oda.children(null, true, -1);
        try {
            LOG.debug(oda.getFile("012YP5EGF7GLCCXV5EVNB2QJAYVRLLUEUZ", true).toString());
//            LOG.debug(oda.getPermissions(new String().repositoryName("od1").relativeId("012YP5EGF7GLCCXV5EVNB2QJAYVRLLUEUZ")).toString());
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }
    
    
    public ESFileList children (String parentId, boolean addPermissions, int maxSize) {
        String result = null;
        UriBuilder builder = UriBuilder.fromPath(BASE_URL);
        if (maxSize!=-1) {
            builder.queryParam("top", maxSize);
        }
        if (parentId!=null && !parentId.isEmpty()) {
            result = exchangeUri(builder.path(ITEM_CHILDREN_URL).build(parentId)).getBody();
        }
        else {
            result = exchangeUri(builder.path(ROOT_CHILDREN_URL).build()).getBody();
        }
        LOG.debug(result);
        ESFileList fileList = null;
        if (result != null) {
            fileList = new ESFileList();
            JSONObject obj;
            try {
                obj = new JSONObject(result);
                JSONArray array = obj.getJSONArray("value");
                for(int i = 0 ; i < array.length() ; i++){
                    ESFile file = getESFileFromJSONObject(array.getJSONObject(i));
                    if (addPermissions) {
                        try {
                            file.permissions(getPermissions(file.getId()));
                        } catch (IOException e) {
                            LOG.error(e.getMessage(),e);
                        }
                    }
                    fileList.addItemsItem(file);
                }
            } catch (JSONException e) {
                LOG.error(e.getMessage(),e);
            }
            
        }
        LOG.debug(fileList.toString());
        return fileList;
    }



    public void downloadTo(String fileId, OutputStream outputstream) throws IOException {
        // This call was the one documented for OneDrive but the stream returned is not well formatted
        // https://dev.onedrive.com/items/download.htm 
//        URI uri = UriBuilder.fromPath(BASE_URL).path(GET_ITEM_CONTENT).build(fileId.getRelativeId());
//        ResponseEntity<String> response = getForObject(uri);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBody().getBytes())) {
//                org.apache.tomcat.util.http.fileupload.IOUtils.copy(inputStream, outputstream);
//            }
//        }
//        else if (response.getStatusCode() == HttpStatus.FOUND) {
//            HttpHeaders headers = response.getHeaders();
//            URI downloadURI = headers.getLocation();
//            try (InputStream is = getFileInputStream(downloadURI)) {
//                org.apache.tomcat.util.http.fileupload.IOUtils.copy(is, outputstream);
//            }
//        }
        URI uri = UriBuilder.fromPath(BASE_URL).path(ITEM_URL).build(fileId);
        String result = exchangeUri(uri).getBody();
        if (result != null) {
            LOG.debug(result);
            JSONObject obj;
            try {
                obj = new JSONObject(result);
                if (obj.has(Item.FILE)) {
                    try (InputStream is = getFileInputStream(new URI(obj.getString(Item.DOWNLOAD_URL)))) {
                        org.apache.tomcat.util.http.fileupload.IOUtils.copy(is, outputstream);
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }


    public String insert(String parentId, InputStream in, String name, String description) throws IOException {
        // we're inserting a folder
        if (in == null) {
            return insertFolder(parentId, name, description);
        }
        // we're inserting a file
        else {
            return insertFolder(parentId, in, name, description);
        }
    }


    public List<ESPermission> getPermissions(String  fileId) throws IOException {
        URI uri = UriBuilder.fromPath(BASE_URL).path(ITEM_PERMISSIONS_URL).build(fileId);
        String result = exchangeUri(uri).getBody();
        List<ESPermission> permissions = null;
        if (result != null) {
            permissions = new ArrayList<>();
            JSONObject obj;
            try {
                obj = new JSONObject(result);
                JSONArray array = obj.getJSONArray("value");
                for(int i = 0 ; i < array.length() ; i++){
                    permissions.add(getESPermissionFromJSONObject(array.getJSONObject(i)));
                }
            } catch (JSONException e) {
                LOG.error(e.getMessage(),e);
            }
        }
        return permissions;
    }
    
    


    public void delete(String fileId) throws IOException {
        URI uri = UriBuilder.fromPath(BASE_URL).path(ITEM_URL).build(fileId);
        ResponseEntity<String> response = exchangeUri(uri, HttpMethod.DELETE);
        if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new IOException("CannotDeleteThisFile");
        }
    }


    public ESFile getFile(String fileId, boolean addPermissions) throws IOException {
        URI uri = UriBuilder.fromPath(BASE_URL).path(ITEM_URL).build(fileId);
        String result = exchangeUri(uri).getBody();
        ESFile file = null;
        if (result != null) {
            LOG.debug(result);
            JSONObject obj;
            try {
                obj = new JSONObject(result);
                file = getESFileFromJSONObject(obj);
                if (addPermissions) {
                    try {
                        file.permissions(getPermissions(fileId));
                    } catch (IOException e) {
                        LOG.error(e.getMessage(),e);
                    }
                }
            } catch (JSONException e) {
                LOG.error(e.getMessage(),e);
            }
                
        }
        return file;
    }

//
//    @Override
//    public void update(String fileId, InputStream in, String description) throws IOException {
//        // TODO Auto-generated method stub
//        
//    }
//
//
//    @Override
//    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
//        // TODO Auto-generated method stub
//        
//    }
    
    
    private String insertFolder(String parentId, String name, String description) {
        URI uri = null;
        if (parentId != null && !parentId.isEmpty()) {
            uri = UriBuilder.fromPath(BASE_URL).path(ITEM_CHILDREN_URL).build(parentId);
        }
        else {
            uri = UriBuilder.fromPath(BASE_URL).path(ROOT_CHILDREN_URL).build();
        }
        /*
         * POST /drive/root/children  OR /drive/items/{parent-id}/children
            Content-Type: application/json
            
            {
            "name": "FolderA",
            "folder": { }
            }
         */
        try {
            JSONObject obj = new JSONObject();
            obj.put(Item.NAME, name);
            obj.put(Item.FOLDER, "");
            exchangeUri(uri, HttpMethod.POST, obj);
        } catch (JSONException e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }


    private String insertFolder(String parentId, InputStream in, String name, String description) {
        // TODO Auto-generated method stub
        return null;
    }

    
    /* *********************  HTTP METHODS *************** */
    private ResponseEntity<String> exchangeUri(URI url, Object...params) {
        return exchangeUri(url, HttpMethod.GET, params);
    }
    
    private ResponseEntity<String> exchangeUri(URI url, HttpMethod method, Object...params) {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer "+ACCESS_TOKEN);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        RestTemplate restTemplate = new RestTemplate();
        LOG.debug(url.toString());
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), method, entity, String.class, params);
        return response;
    }


    /* *********************  JSON DESERIALIZE METHODS *************** */
    
    protected static final class Item {
        /*
         * A file item 
            {
                "@microsoft.graph.downloadUrl": "https://everteamsoftware365-my.sharepoint.com/personal/k_bennat_everteamsoftware365_onmicrosoft_com/_layouts/15/download.aspx?UniqueId=29be4d1e-13e5-47e0-b56d-feaced1b9fe6&Translate=false&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAvZXZlcnRlYW1zb2Z0d2FyZTM2NS1teS5zaGFyZXBvaW50LmNvbUBhYmNhMGRlMy0yMGJmLTQwODEtYmRkMS1hYjY2YjA3NWMxNTQiLCJpc3MiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAiLCJuYmYiOjE0OTAxNzM0NTQsImV4cCI6MTQ5MDE3NzA1NCwiZW5kcG9pbnR1cmwiOiI3SjhEV0RkekVSKytKZXN3VW5kRkpLQktrNlZHeDhROXNEZFNNd2kvbjJRPSIsImVuZHBvaW50dXJsTGVuZ3RoIjoiMTg3IiwiaXNsb29wYmFjayI6IlRydWUiLCJuYW1laWQiOiIwIy5mfG1lbWJlcnNoaXB8ay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20iLCJuaWkiOiJtaWNyb3NvZnQuc2hhcmVwb2ludCIsImlzdXNlciI6InRydWUiLCJjYWNoZWtleSI6IjBoLmZ8bWVtYmVyc2hpcHwxMDAzN2ZmZTlmZGYxYTJiQGxpdmUuY29tIiwidXNlUGVyc2lzdGVudENvb2tpZSI6bnVsbH0.yK8U-D8JRHUAuuPYYdEomDgiujTHOqupgF9PnLL6zWU&prooftoken=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImpOX1RsZ1otUUk0UHZpc2pTVnpKMW9ySnRnOCJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDBAKiIsImlzcyI6IjAwMDAwMDAzLTAwMDAtMGZmMS1jZTAwLTAwMDAwMDAwMDAwMEAqIiwibmJmIjoiMTQ5MDEzMDUzMyIsImV4cCI6IjE0OTA3MzUzMzMiLCJwcmYiOiJncXNybjZXNnlkTzBxcnZGY1JPZlEybFc2ZlpiSGp2WVYvWmtDd2QxYk8xUHQ3VFJMd1hjRU9YSVl2OXAvYk5XVkJwTnZQSm84SFoxUVJYeGtEcnJ1M1Y1bGhieGh4czdwRCtIeTJyUjdwbDVZUnErYWRkWGtveHVKUG5TZVRBT05KVFFGa0F6ZVFBNllVNFZuME1GQ0ZDL2FWRXRpUnZveXJNV1dNblhwS2pQSTlNYW5kN1BXRGYra3oxQlFUeFhybGtiVURhamFONnRyWmYvWHVaZTVQVEpYQW9ScjYxWGhXWWlhd0Q3N0RCa1hFc1ROZVBydGhieVlDelVtOVRFdjR0VHh5Y0FOR1dvSmd1NTJKdTE4dEVsTXpoYWxPdzI0R2hBa29pSWVhZzdiUkpBRHhTZ0RDWVFKL1BkUkJhZ1dmaENhVmdBRkRSUjZMaUdnakxSSXc9PSIsImlzdXNlciI6InRydWUifQ.BGiohUK-x06ORGgZQpBTd-24zbonfMAVJVIR090JHuVCGFEe4WxQKTz0lR7s8jeEiLPs4z12XkWz1LUx3Qv6a-cn50xVfhk2yzQ5D2CrXODUxowgrNsDrpg3TfOjvIlt1uUd5ZfN4s33GDx6gUths71r1srHM1LUD6hFJ_sjYvp7JzDzshq7K8LzNnDu2nSuf_8ndx3OCckygCM9nuxQd-FVBkbAEAgSpgK8QSaB4T1H5I-AruJ90Z9l7i0dY_WpozDOF1XRAd5LLUk_47kjsdpwDdte_tnloLz2Xs8-WUw-DyvfQPvEEHoQ8E1aFAhY0NxxSsiApc7XDTVryZCZrw",
                "createdBy": {
                    "user": {
                        "id": "8dd5c724-7035-48a5-9481-0ca40cbe7318",
                        "displayName": "Kader Bennat"
                    }
                },
                "createdDateTime": "2017-03-21T14:47:12Z",
                "eTag": "\"{29BE4D1E-13E5-47E0-B56D-FEACED1B9FE6},1\"",
                "id": "012YP5EGA6JW7CTZIT4BD3K3P6VTWRXH7G",
                "lastModifiedBy": {
                    "user": {
                        "id": "8dd5c724-7035-48a5-9481-0ca40cbe7318",
                        "displayName": "Kader Bennat"
                    }
                },
                "lastModifiedDateTime": "2017-03-21T14:47:12Z",
                "name": "Desert.jpg",
                "webUrl": "https://everteamsoftware365-my.sharepoint.com/personal/k_bennat_everteamsoftware365_onmicrosoft_com/Documents/Desert.jpg",
                "cTag": "\"c:{29BE4D1E-13E5-47E0-B56D-FEACED1B9FE6},1\"",
                "file": {
                    "hashes": {
                        "quickXorHash": "KI/og7aU26KaWWwGeIom7NXigTQ="
                    }
                },
                "fileSystemInfo": {
                    "createdDateTime": "2017-03-21T14:47:12Z",
                    "lastModifiedDateTime": "2017-03-21T14:47:12Z"
                },
                "image": {},
                "parentReference": {
                    "driveId": "b!kkEY-OuMtUyR3G9XJuVtiYSU-chcIHpLp6x9vXqZOxvhr2cySdbmTaywovS6o4if",
                    "id": "012YP5EGF6Y2GOVW7725BZO354PWSELRRZ",
                    "path": "/drive/root:"
                },
                "photo": {
                    "takenDateTime": "2008-03-14T13:59:26Z"
                },
                "size": 845941
            }
         */   
         /* A folder item
         * 
          {
              "createdBy": {
                "user": {
                  "id": "8dd5c724-7035-48a5-9481-0ca40cbe7318",
                  "displayName": "Kader Bennat"
                }
              },
              "createdDateTime": "2017-03-22T09:21:24Z",
              "eTag": "\"{D9AEA7C3-98BA-4221-AF8F-8DD0C3A9FD87},1\"",
              "id": "012YP5EGGDU6XNTOUYEFBK7D4N2DB2T7MH",
              "lastModifiedBy": {
                "user": {
                  "id": "8dd5c724-7035-48a5-9481-0ca40cbe7318",
                  "displayName": "Kader Bennat"
                }
              },
              "lastModifiedDateTime": "2017-03-22T09:21:24Z",
              "name": "Test TTR",
              "webUrl": "https://everteamsoftware365-my.sharepoint.com/personal/k_bennat_everteamsoftware365_onmicrosoft_com/Documents/Test%20TTR",
              "cTag": "\"c:{D9AEA7C3-98BA-4221-AF8F-8DD0C3A9FD87},0\"",
              "fileSystemInfo": {
                "createdDateTime": "2017-03-22T09:21:24Z",
                "lastModifiedDateTime": "2017-03-22T09:21:24Z"
              },
              "folder": {
                "childCount": 0
              },
              "parentReference": {
                "driveId": "b!kkEY-OuMtUyR3G9XJuVtiYSU-chcIHpLp6x9vXqZOxvhr2cySdbmTaywovS6o4if",
                "id": "012YP5EGF6Y2GOVW7725BZO354PWSELRRZ",
                "path": "/drive/root:"
              },
              "size": 0
          }
         */
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CREATION_TIME = "createdDateTime";
        public static final String SIZE = "size";
        public static final String LAST_MODIFIED_DATE = "lastModifiedDateTime";
        public static final String LAST_MODIFIED_BY = "lastModifiedBy";
        public static final String FOLDER = "folder";
        public static final String FILE = "file";
        public static final String DOWNLOAD_URL = "@microsoft.graph.downloadUrl";
    }
    
    protected static final class User {
        public static final String TAG_NAME = "user";
        public static final String ID = "id";
        public static final String DISPLAY_NAME = "displayName";
    }
    
    private ESFile getESFileFromJSONObject(JSONObject jsonObject) {
        ESFile file = null;
        try {
            String id = jsonObject.getString(Item.ID);
            String name = jsonObject.getString(Item.NAME);
            OffsetDateTime created = OffsetDateTime.parse(jsonObject.getString(Item.CREATION_TIME));
            OffsetDateTime lastModified = OffsetDateTime.parse(jsonObject.getString(Item.LAST_MODIFIED_DATE));
            boolean isFolder = jsonObject.has(Item.FOLDER);
            Long size = jsonObject.getLong(Item.SIZE);
            JSONObject lastModifiedBy = jsonObject.getJSONObject(Item.LAST_MODIFIED_BY).getJSONObject(User.TAG_NAME);
            String lastModifiedById = lastModifiedBy.getString(User.ID);
            String lastModifiedByName = lastModifiedBy.getString(User.DISPLAY_NAME);
            String downloadUrl = null;
            String md5 = null;
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            file = new ESFile();
            
            file
                .id(id)
                .name(name)
                .creationTime(created)
                .directory(isFolder)
                .fileSize(size)
                .lastModifiedTime(lastModified)
                .lastModifiedUser(new ESUser().id(lastModifiedById).displayName(lastModifiedByName))
                .mimeType(fileNameMap.getContentTypeFor(name))
//                .owners()
//                .permissions(permissions)
                ;
            
            if (!isFolder) {
                downloadUrl = jsonObject.getString(Item.DOWNLOAD_URL);
                md5 = DigestUtils.md5DigestAsHex(getFileInputStream(new URI(downloadUrl)));
                file.checksum(md5);
            }
        } catch (JSONException e) {
            LOG.error(e.getMessage(),e);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        } catch (URISyntaxException e) {
            LOG.error(e.getMessage(),e);
        }
        return file;
    }

    protected static final class Permission {
        /*
         * A permission JSON Object
         *  {
              "id": "OEJBNjA4Q0MtN0Q2MC00RjEzLUEyNzItOEIzQUI5M0ZCOEQ3",
              "roles": [
                "write"
              ],
              "link": {
                "scope": "anonymous",
                "type": "edit",
                "webUrl": "https://everteamsoftware365-my.sharepoint.com/personal/k_bennat_everteamsoftware365_onmicrosoft_com/_layouts/15/guestaccess.aspx?docid=12bc432bfa4d743aba82418ac56ba1299&authkey=ARzol0EQgoPw5agvXMU4DLg"
              }
            },
            {
              "grantedTo": {
                "user": {
                  "id": "8dd5c724-7035-48a5-9481-0ca40cbe7318",
                  "displayName": "Kader Bennat"
                }
              },
              "id": "aTowIy5mfG1lbWJlcnNoaXB8ay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20",
              "roles": [
                "write"
              ]
            }
         */
        public static final String ID = "id";
        public static final String GRANTED_TO = "grantedTo";
        public static final String LINK = "link";
        public static final String ROLES = "roles";
    }
    protected static final class Link {
        public static final String SCOPE = "scope";
        public static final String TYPE = "type";
        public static final String WEB_URL = "webUrl";
    }
    
    private ESPermission getESPermissionFromJSONObject(JSONObject jsonObject) {
        ESPermission permission = null;
        try {
            permission = new ESPermission();
//            String id = jsonObject.getString(Permission.ID);
            JSONArray roles = jsonObject.getJSONArray(Permission.ROLES);
            for (int i=0;i<roles.length();i++) {
                String role = roles.getString(i);
                switch (role) {
                    case "write":
                        permission.addRolesItem(RolesEnum.WRITER)
                            .type(TypeEnum.ALLOW);
                        break;
                    default:
                        break;
                }
            }
            if (jsonObject.has(Permission.GRANTED_TO)) {
                JSONObject grantedTo = jsonObject.getJSONObject(Permission.GRANTED_TO).getJSONObject(User.TAG_NAME);
                permission.userId(grantedTo.getString(User.ID))
                    .accountType(AccountTypeEnum.USER);
            }
            if (jsonObject.has(Permission.LINK)) {
                JSONObject link = jsonObject.getJSONObject(Permission.LINK);
                String scope = link.getString(Link.SCOPE);
                switch (scope) {
                    case "anonymous":
                        permission.accountType(AccountTypeEnum.ANYONE);
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            LOG.error(e.getMessage(),e);
        }
        return permission;
    }
    
    private InputStream getFileInputStream(URI downloadURI) throws IOException {
        UrlResource resource = new UrlResource(downloadURI);
        return resource.getInputStream();
    }
}
