package com.everteam.storage.drive;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository.TypeEnum;
import com.everteam.storage.services.RepositoryService;
import com.everteam.storage.utils.FileInfo;


public abstract class DriveTest {

    
    private static final String FILENAME = "ES Descriptif des services de base.pdf";
    private static final String CODE = "Drive_Test";
    private static Path PATH;
    private static FileInfo FILEINFO;
    
    private IDrive testingDrive;
    
    public String folderId;
        
    @Autowired
    RepositoryService repositoryService;
    
    @Rule 
    public TestName name = new TestName();
    
    @BeforeClass
    public static void setup() throws IOException {
        Resource resource = new ClassPathResource(FILENAME);
        File file = resource.getFile();
        PATH = file.toPath();
        FILEINFO = new FileInfo("FName_" + CODE,
                "FDescription_" + CODE,
                "application/pdf",
                Files.size(PATH),
                null);
    }
    
    @Before
    public  void init() throws IOException, GeneralSecurityException {
        
        if (testingDrive == null) {
            List<IDrive> drives = repositoryService.getDriveList();
            for (IDrive drive : drives) {
                if (drive.getRepository().getType().equals(getDriveType())) {
                    testingDrive = drive;
                    if (testingDrive instanceof OAuth2DriveImpl) {
                        ((OAuth2DriveImpl) testingDrive).authorize();
                    }
                    break;
                }
            }
        }
        
        if (testingDrive == null) {
            throw new NoSuchBeanDefinitionException(getDriveType().toString());
        }
        
        folderId = testingDrive.insertFolder(null, name.getMethodName(), name.getMethodName() + "_"+ OffsetDateTime.now().toString());
    }
    
    
    protected abstract TypeEnum getDriveType();

    @After
    public void clean() throws IOException {
        testingDrive.delete(folderId);
    }
    
    
    
    @Test
    public void testChildren() throws IOException {
        createFile();
        createFolder();
        ESFileList files = testingDrive.children(folderId, false, false, 100);
        // Assert
        assertEquals(2, files.getItems().size());
    }
    
    @Test
    public void testdownloadTo() throws IOException {
        String fileId = createFile();
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        testingDrive.downloadTo(fileId, baOs);
        
        assertArrayEquals(baOs.toByteArray(), Files.readAllBytes(getPath()));
    }
    
    
    @Test
    public void testGetPermissions() throws IOException {
        List<ESPermission> permissions = testingDrive.getPermissions(folderId);
        
        assertTrue(permissions.size()>=0);
    }
    
    
    @Test
    public void testDelete() throws IOException {
        String fileId = createFile();
        testingDrive.delete(fileId);
        
        
        assertFalse(testingDrive.exists(fileId));
    }
    
    @Test
    public void testGetFile() throws IOException {
        OffsetDateTime beforeCreate = OffsetDateTime.now();
        
        String fileId = createFile();
        OffsetDateTime afterCreate = OffsetDateTime.now();
        
        ESFile file = testingDrive.getFile(fileId, true, true);
        
        assertFalse(file.getDirectory());
        assertEquals(FILEINFO.getName(), file.getName());
        assertEquals(Files.size(getPath()), file.getFileSize().longValue());
        assertEquals(folderId, file.getParents().get(0).getId());
        
        assertEquals(DigestUtils.md5DigestAsHex(Files.newInputStream(getPath())), file.getChecksum());
        
        //System.out.println("FileCreationTime :"  + file.getCreationTime().toString());
        //System.out.println("Now :"  + OffsetDateTime.now().toString());
        assertTrue(file.getCreationTime().isBefore(afterCreate.plusSeconds(5)));
        assertTrue(file.getCreationTime().plusSeconds(5).isAfter(beforeCreate));
                
        assertEquals(folderId, file.getParents().get(0).getId());
        assertTrue(file.getPermissions().size()>=0);        
    }
    
    
    @Test
    public void testUpdate() throws IOException {
        
        
    }
    
   
    
    
    @Test
    public void testInsertFile() throws IOException {
        String fileId = createFile();
        
        assertTrue(testingDrive.exists(fileId));
        
        ESFile file = testingDrive.getFile(fileId, true, true);
        assertFalse(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    
    @Test
    public void testInsertFolder() throws IOException {
        String fileId = createFolder();
        
        ESFile file = testingDrive.getFile(fileId, true, true);
        assertTrue(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    @Test
    public void testIsFolder() throws IOException {
        String fileId = createFile();
        assertFalse(testingDrive.isFolder(fileId));
        
        String folderId = createFolder();
        assertTrue(testingDrive.isFolder(folderId));
    }
    
    
    private String createFile() throws IOException {
        FILEINFO.setInputStream(Files.newInputStream(PATH));
        return  testingDrive.insertFile(folderId, FILEINFO);
        
    }
    
    private String createFolder() throws IOException {
        return testingDrive.insertFolder(folderId, "DName_"+  CODE, "DDescription_" + CODE);
    }
    
    
    private Path getPath() throws IOException {
        return PATH;
    }
    
}
