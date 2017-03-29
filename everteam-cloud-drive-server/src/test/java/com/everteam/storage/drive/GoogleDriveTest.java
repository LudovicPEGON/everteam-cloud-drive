package com.everteam.storage.drive;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.DigestUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.utils.FileInfo;



@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest()
public class GoogleDriveTest {
    private static final Path PATH = Paths.get("D:\\ludo\\FichiersTests\\JPEG\\homer.jpg");
    
    @Autowired
    @Qualifier("google")
    private DriveImpl   googledrive;

    
    @Rule 
    public TestName name = new TestName();
    
    
  
    public String folderId;
    
    
    @Before
    public void setup() throws IOException {
        ESRepository repository = new ESRepository();
        repository.setName("lpelpe");
        repository.clientSecrets("/client_secret.json");
        repository.setRootDirectory("0B8V5PhJl48JdbU93VFdEZG43MDQ");
        googledrive.init(repository);
        
        folderId = googledrive.insertFolder(null, name.getMethodName(), name.getMethodName() + "_"+ OffsetDateTime.now().toString());
    }
    
    
    @After
    public void clean() throws IOException {
        googledrive.delete(folderId);
    }
    
    
    
    @Test
    public void testChildren() throws IOException {
        createFile();
        createFolder();
        ESFileList files = googledrive.children(folderId, false, false, 100);
        // Assert
        assertEquals(2, files.getItems().size());
    }
    
    @Test
    public void testdownloadTo() throws IOException {
        String fileId = createFile();
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        googledrive.downloadTo(fileId, baOs);
        
        assertArrayEquals(baOs.toByteArray(), Files.readAllBytes(PATH));
    }
    
    
    @Test
    public void testGetPermissions() throws IOException {
        List<ESPermission> permissions = googledrive.getPermissions(folderId);
        
        assertEquals(1, permissions.size());
    }
    
    
    @Test
    public void testDelete() throws IOException {
        String fileId = createFile();
        googledrive.delete(fileId);
        
        
        assertFalse(googledrive.exists(fileId));
    }
    
    @Test
    public void testGetFile() throws IOException {
        OffsetDateTime beforeCreate = OffsetDateTime.now();
        
        String fileId = createFile();
        ESFile file = googledrive.getFile(fileId, true, true);
        
        assertFalse(file.getDirectory());
        assertEquals(PATH.getFileName(), file.getName());
        assertEquals(Files.size(PATH), file.getFileSize().longValue());
        assertEquals(folderId, file.getDirectory());
        
        assertEquals(DigestUtils.md5DigestAsHex(Files.newInputStream(PATH)), file.getChecksum());
        assertTrue(file.getCreationTime().isBefore(OffsetDateTime.now()));
        assertTrue(file.getCreationTime().isAfter(beforeCreate));
                
        assertEquals(folderId, file.getParents().get(0).getId());
        assertTrue(file.getPermissions().size()>0);        
    }
    
    
    @Test
    public void testUpdate() throws IOException {
        
        
    }
    
   
    
    
    @Test
    public void testInsertFile() throws IOException {
        String fileId = createFile();
        
        assertTrue(googledrive.exists(fileId));
        
        ESFile file = googledrive.getFile(fileId, true, true);
        assertFalse(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    
    @Test
    public void testInsertFolder() throws IOException {
        String fileId = createFolder();
        
        ESFile file = googledrive.getFile(fileId, true, true);
        assertTrue(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    @Test
    public void testIsFolder() throws IOException {
        String fileId = createFile();
        assertFalse(googledrive.isFolder(fileId));
        
        String folderId = createFolder();
        assertTrue(googledrive.isFolder(folderId));
    }
    
    
    private String createFile() throws IOException {
        final FileInfo fileInfo = new FileInfo("FName_fé'(-è_ççà)=~#{[|`^@]}",
                "FDescription_fé'(-è_ççà)=~#{[|`^@]}",
                "image/jpeg",
                Files.size(PATH),
                Files.newInputStream(PATH)
        );
        
        return  googledrive.insertFile(folderId, fileInfo);
    }
    
    private String createFolder() throws IOException {
        return googledrive.insertFolder(folderId, "DName_fé'(-è_ççà)=~#{[|`^@]}", "DDescription_fé'(-è_ççà)=~#{[|`^@]}");
    }
    
    
    

}
