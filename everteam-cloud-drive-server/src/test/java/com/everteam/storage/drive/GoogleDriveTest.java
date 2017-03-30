package com.everteam.storage.drive;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository.TypeEnum;
import com.everteam.storage.services.RepositoryService;
import com.everteam.storage.utils.FileInfo;



@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleDriveTest {
    
    private static final String FILENAME = "/ES Descriptif des sérvices de base.pdf";
    private static final String CODE = "fé'(-è_ççà)=~#{[|`^@]}";
    private static Path PATH;
    private static FileInfo FILEINFO;
    
    private static IDrive googledrive;
    
    public String folderId;
        
    @Autowired
    RepositoryService repositoryService;
    
    @Rule 
    public TestName name = new TestName();
    
    @BeforeClass
    public static void setup() throws IOException  {
        try {
            PATH = Paths.get(GoogleDriveTest.class.getResource(FILENAME).toURI());
            FILEINFO = new FileInfo("FName_" + CODE,
                    "FDescription_" + CODE,
                    "application/pdf",
                    Files.size(PATH),
                    null);
            
            
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
    
    @Before
    public  void init() throws IOException {
        
        if (googledrive == null) {
            List<IDrive> drives = repositoryService.getDriveList();
            for (IDrive drive : drives) {
                if (drive.getRepository().getType().equals(TypeEnum.GOOGLE)) {
                    googledrive = drive;
                    break;
                }
            }
        }
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
        
        assertArrayEquals(baOs.toByteArray(), Files.readAllBytes(getPath()));
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
        //OffsetDateTime beforeCreate = OffsetDateTime.now();
        
        String fileId = createFile();
        ESFile file = googledrive.getFile(fileId, true, true);
        
        assertFalse(file.getDirectory());
        assertEquals(FILEINFO.getName(), file.getName());
        assertEquals(Files.size(getPath()), file.getFileSize().longValue());
        assertEquals(folderId, file.getParents().get(0).getId());
        
        assertEquals(DigestUtils.md5DigestAsHex(Files.newInputStream(getPath())), file.getChecksum());
        assertTrue(file.getCreationTime().isBefore(OffsetDateTime.now()));
        //assertTrue(file.getCreationTime().isAfter(beforeCreate) || file.getCreationTime().isEqual(beforeCreate));
                
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
        FILEINFO.setInputStream(Files.newInputStream(PATH));
        return  googledrive.insertFile(folderId, FILEINFO);
        
    }
    
    private String createFolder() throws IOException {
        return googledrive.insertFolder(folderId, "DName_"+  CODE, "DDescription_" + CODE);
    }
    
    
    private static Path getPath() throws IOException {
        try {
            return Paths.get(GoogleDriveTest.class.getResource(FILENAME).toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

}
