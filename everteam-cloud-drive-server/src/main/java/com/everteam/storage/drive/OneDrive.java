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
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.onedrive.OneDriveClientAPI;
import com.everteam.storage.utils.FileInfo;

@Component
@Scope("prototype")
public class OneDrive extends DriveImpl {

    private final static String ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKXzZqVUMxLVNPX3BiaXIyWnNGZmJUSy1VQ3lmNEdfWlI0QUJuczQzM1A1UHItUi1sZFpWeUt3QzJPdXVVNkZZbV82THd3ZnVwNmJqWnJGUUVwTjdxaGlBQSIsImFsZyI6IlJTMjU2IiwieDV0IjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIiwia2lkIjoiYTNRTjBCWlM3czRuTi1CZHJqYkYwWV9MZE1NIn0.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9hYmNhMGRlMy0yMGJmLTQwODEtYmRkMS1hYjY2YjA3NWMxNTQvIiwiaWF0IjoxNDkwNjE4MDgwLCJuYmYiOjE0OTA2MTgwODAsImV4cCI6MTQ5MDYyMTk4MCwiYWNyIjoiMSIsImFpbyI6IkFRQUJBQUVBQUFEUk5ZUlEzZGhSU3JtLTRLLWFkcENKaU5rTXEtaWJzVE9wejMtQU1ETXRfeDVHZld6Q1BSVHJsT0lJdHQyRDBPb3V5TU0xVElBM3JXekRodUhoRXFWQzc1WF82VnRyUG5RMkVocWNSZkJib01YZXAyY1poejE4a1BRdzFTV2tlWU1nQUEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Ik9uZURyaXZlU3RvcmFnZSIsImFwcGlkIjoiZTQ4ODAyODEtN2JjNi00OGM0LTlhOTUtNGIwZWU5ZmIwY2JhIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJCZW5uYXQiLCJnaXZlbl9uYW1lIjoiS2FkZXIiLCJpcGFkZHIiOiI5MC44NS4yMDMuMTc3IiwibmFtZSI6IkthZGVyIEJlbm5hdCIsIm9pZCI6IjhkZDVjNzI0LTcwMzUtNDhhNS05NDgxLTBjYTQwY2JlNzMxOCIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzN0ZGRTlGREYxQTJCIiwic2NwIjoiRmlsZXMuUmVhZFdyaXRlIEZpbGVzLlJlYWRXcml0ZS5BbGwgVXNlci5SZWFkIiwic2lnbmluX3N0YXRlIjpbImttc2kiXSwic3ViIjoiRTBBOGtLYWZ2MXFCUE51bjNCOTNJckctbXMweHFLc0EyNC02NlNzaVZlYyIsInRpZCI6ImFiY2EwZGUzLTIwYmYtNDA4MS1iZGQxLWFiNjZiMDc1YzE1NCIsInVuaXF1ZV9uYW1lIjoiay5iZW5uYXRAZXZlcnRlYW1zb2Z0d2FyZTM2NS5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJrLmJlbm5hdEBldmVydGVhbXNvZnR3YXJlMzY1Lm9ubWljcm9zb2Z0LmNvbSIsInZlciI6IjEuMCJ9.X6aMCrAt55HJ0ItJpoCl2uLuD0kb81ZiztYO4NO7eZSGPT5W7wb1edcIOSbF0fNk3IO1pDEmJw4zKuxg-KzuwpoDaqq1_ctOKoo7E2pEse4Q_3CEzLZmCAc9yrYR6rdqWmRopvoXtm4unIVbaggOAce_UU9ycLYso-hcqfGiPHRPV4gK-zO2ClvT2svygwystA0iSExcTn5GFmie_1ga3ScAWSx8yN5hISsTYOLaw7zZyg65ms23TvbR54gFLL-f2F4r71neA8fQBTk8NXbAFE_UqLQaqYoCKH1TTvy4g9CGQBIQzO4X6fmcinGaPvwA4yQOy_KMABqlLAhT38TTxQ";
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
    public void update(String fileId, String name, String contentType, InputStream in, String description) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkUpdates(String fileId, OffsetDateTime fromDate, Consumer<ESFile> consumer) throws IOException {
        // TODO Auto-generated method stub

    }
    
}
