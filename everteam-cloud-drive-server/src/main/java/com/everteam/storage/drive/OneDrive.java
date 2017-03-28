package com.everteam.storage.drive;

import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.onedrive.OneDriveClientAPI;
import com.everteam.storage.utils.FileInfo;

@Component
@Scope("prototype")
public class OneDrive extends DriveImpl {

    private final static String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKUVF2M0ZZZ0ZNWjFtOW9IRVFNdW5Bb3lGWFZud3BnTzBzT2w2OUdCUWp6R0VtLVV4Y05oc19iRDNDZzd3VllpZFRqSkNZd0o5N3M0TEFDRlN1aFRNY2lBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIiwia2lkIjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9hYmNhMGRlMy0yMGJmLTQwODEtYmRkMS1hYjY2YjA3NWMxNTQvIiwiaWF0IjoxNDkwNzA0MzEzLCJuYmYiOjE0OTA3MDQzMTMsImV4cCI6MTQ5MDcwODIxMywiYWNyIjoiMSIsImFpbyI6IkFRQUJBQUVBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKMEN0VzJDZVlqRnJjeVFwNXhVYXUzMnREYlR1R0t5VEVZQmZ3bzFyZjlKMl9RRWdfOXdQdlJSZ3lYR05SRjhLRWRqcTY3aVNHY1lzNlFROVFjaGZ5NHd4d3VfYWdMM3NTYmJTcVVQTlF1a2NnQUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Ik9uZURyaXZlU3RvcmFnZSIsImFwcGlkIjoiZTQ4ODAyODEtN2JjNi00OGM0LTlhOTUtNGIwZWU5ZmIwY2JhIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJCZW5uYXQiLCJnaXZlbl9uYW1lIjoiS2FkZXIiLCJpcGFkZHIiOiI5MC44NS4yMDMuMTc3IiwibmFtZSI6IkthZGVyIEJlbm5hdCIsIm9pZCI6IjhkZDVjNzI0LTcwMzUtNDhhNS05NDgxLTBjYTQwY2JlNzMxOCIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzN0ZGRTlGREYxQTJCIiwic2NwIjoiRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5BbGwgVXNlci5SZWFkIiwic3ViIjoiRTBBOGtLYWZ2MXFCUE51bjNCOTNJckctbXMweHFLc0EyNC02NlNzaVZlYyIsInRpZCI6ImFiY2EwZGUzLTIwYmYtNDA4MS1iZGQxLWFiNjZiMDc1YzE1NCIsInVuaXF1ZV9uYW1lIjoiay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJrLmJlbm5hdEBldmVydGVhbXNvZnR3YXJlMzY1Lm9ubWljcm9zb2Z0LmNvbSIsInZlciI6IjEuMCJ9.DEuuZxCzFLZ40INIGDjEajeYnHFOYYCRKqGraURDKcg4Qd6usOlLMqtg0K0l50VHCRQUFX6hRqTLmwszD8JgTWAw9i69Zd3UdqaDZdzpg0_Ex0NIKh212AWhIPnccWY2DpCemSrKQ8NB8QreaGwHm16QcNy-Tl6E2i1OBsjVdBuxZ5yU5hLLL3Bdk4bTqvRRc4SPOpT2DZiXalCyPO9W6A7hpMn40kfoP2T4bQqLrPTdlFvncyY9QsuWvV9WUv5jbn0QH-e7_4or1yFo69vr2JBJ3jn08gB6UcIafpSmKsHx9GQykYOtn5exdr5tAAD0v89wxz59PcJt0zVZXq2-Lw";
    private OneDriveClientAPI         api;

    @Override
    public void init(ESRepository repository) {
        super.init(repository);
        api = new OneDriveClientAPI(ACCESS_TOKEN);
    }

    @Override
    public ESFileList children(String parentId, boolean addPermissions, boolean addChecksum, int maxSize) throws IOException {
        return api.children(parentId, addPermissions, maxSize);
    }

    @Override
    public void downloadTo(String fileId, OutputStream outputstream) throws IOException {
        api.downloadTo(fileId, outputstream);
    }

    @Override
    public String insertFile(String parentId, FileInfo info) throws IOException {
        return api.insertFile(parentId, info);
    }
    
    @Override
    public String insertFolder(String parentId, String name, String description) throws IOException {
        return api.insertFolder(parentId, name, description);
    }
    
    @Override
    public boolean isFolder(String fileId) throws IOException {
        return api.isFolder(fileId);
    }

    @Override
    public List<ESPermission> getPermissions(String fileId) throws IOException {
        return api.getPermissions(fileId);
    }

    @Override
    public void delete(String fileId) throws IOException {
        api.delete(fileId);
    }

    @Override
    public ESFile getFile(String fileId, boolean addPermissions,  boolean addChecksum) throws IOException {
        return api.getFile(fileId, addPermissions, addChecksum);
    }

    @Override
    public void update(String fileId, FileInfo info) throws IOException {
        api.update(fileId, info);
    }

    @Override
    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        api.checkUpdates(fileId, fromDate, consumer);

    }
    
}
