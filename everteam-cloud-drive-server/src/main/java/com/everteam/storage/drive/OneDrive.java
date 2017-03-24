package com.everteam.storage.drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileId;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.onedrive.OneDriveClientAPI;

@Component
@Scope("prototype")
public class OneDrive extends DriveImpl {

    private final static String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKUFhmX1J5cDgtLUxvcmg4bkN1Q3ctS0phT0NQTzIwVXlkbGNDYzg2bU0tTXdrNzFuVFVaS05kSFE1eVlLRHJxQ0ZoUjA0NXFWaHRMTEFidzJFbUpZY3lBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIiwia2lkIjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9hYmNhMGRlMy0yMGJmLTQwODEtYmRkMS1hYjY2YjA3NWMxNTQvIiwiaWF0IjoxNDkwMTk3NDE4LCJuYmYiOjE0OTAxOTc0MTgsImV4cCI6MTQ5MDIwMTMxOCwiYWNyIjoiMSIsImFpbyI6IkFRQUJBQUVBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKYjU2Qkl4Sm9HcHZBSlFXTVdSNFVjeXVLTzB0R0ZGQVFTam1uNE1KTEdEY3dMZjByMkhET3VnVEZFeEdzeWZCZ2VtT1VLcGxlUVRTVk92M1lvdGJmLXZaSXoyTVFEallzckt2dkpIclE3WndnQUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Ik9uZURyaXZlU3RvcmFnZSIsImFwcGlkIjoiZTQ4ODAyODEtN2JjNi00OGM0LTlhOTUtNGIwZWU5ZmIwY2JhIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJCZW5uYXQiLCJnaXZlbl9uYW1lIjoiS2FkZXIiLCJpcGFkZHIiOiI5MC44NS4yMDMuMTc3IiwibmFtZSI6IkthZGVyIEJlbm5hdCIsIm9pZCI6IjhkZDVjNzI0LTcwMzUtNDhhNS05NDgxLTBjYTQwY2JlNzMxOCIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzN0ZGRTlGREYxQTJCIiwic2NwIjoiRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5BbGwgVXNlci5SZWFkIiwic2lnbmluX3N0YXRlIjpbImttc2kiXSwic3ViIjoiRTBBOGtLYWZ2MXFCUE51bjNCOTNJckctbXMweHFLc0EyNC02NlNzaVZlYyIsInRpZCI6ImFiY2EwZGUzLTIwYmYtNDA4MS1iZGQxLWFiNjZiMDc1YzE1NCIsInVuaXF1ZV9uYW1lIjoiay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJrLmJlbm5hdEBldmVydGVhbXNvZnR3YXJlMzY1Lm9ubWljcm9zb2Z0LmNvbSIsInZlciI6IjEuMCJ9.uLLx2L9sC_Sb84yakHsclJxIT2IvIjU1AfGYIvT6rrGDlxw6YDjdETURBZ7l5FSdoyJRbboG0X4I39knAX9U5czoplgLyjchwT-hKxry2gIqdUT5Pm7RrsTj4CHXw8OWTMi51tMEstU_XkC5_DITY8q6uLLfdWFa6b_5za7Q9pL0dsG1XiU87L8ijedv3TIdqIIsm11cRUKAmZoKpUp06zJ2A2ftqx8kvIOoAlWqzqIYr5Byq0zSTrXDPvwBUlV8IVc3qDRzdFyxf7fe0obRUP2oeKRPKtlapo7E0Qi2WZ2cgidPVVZkfTfuGb-LeDApottFgTToqNdu8hO6lCuBFQ";
    private OneDriveClientAPI         api;

    @Override
    public void init(ESRepository repository) {
        super.init(repository);
        api = new OneDriveClientAPI(ACCESS_TOKEN);
    }

    @Override
    public ESFileList children(ESFileId parentId, boolean addPermissions, int maxSize) throws IOException {
        return api.children(parentId, addPermissions, maxSize);
    }

    @Override
    public void downloadTo(ESFileId fileId, OutputStream outputstream) throws IOException {
        api.downloadTo(fileId, outputstream);
    }

    @Override
    public ESFileId insert(ESFileId parentId, InputStream in, String name, String description) throws IOException {
        return api.insert(parentId, in, name, description);
    }

    @Override
    public List<ESPermission> getPermissions(ESFileId fileId) throws IOException {
        return api.getPermissions(fileId);
    }

    @Override
    public void delete(ESFileId fileId) throws IOException {
        api.delete(fileId);
    }

    @Override
    public ESFile getFile(ESFileId fileId, boolean addPermissions) throws IOException {
        return api.getFile(fileId, addPermissions);
    }

    @Override
    public void update(ESFileId fileId, InputStream in, String description) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkUpdates(ESFileId fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        // TODO Auto-generated method stub

    }
    
}
