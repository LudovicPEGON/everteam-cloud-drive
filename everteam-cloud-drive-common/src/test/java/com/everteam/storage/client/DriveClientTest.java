package com.everteam.storage.client;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;
import com.everteam.storage.common.model.ESRepository;
import com.everteam.storage.common.model.ESRepository.TypeEnum;

import feign.FeignException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
         })
@DirtiesContext

public class DriveClientTest {
    private static final String CODE = "Drive_Test";
    private static final MockMultipartFile FILE = new MockMultipartFile("FName_"+  CODE + ".txt", "file.txt", "text/plain", "test".getBytes(UTF_8));
    
    private String repositoryId;
    
    public String folderId;
        
    @Autowired
    RepositoriesApi repositoryApiClient;
    
    @Autowired
    FilesApi fileApiClient;
    
    
    @Rule 
    public TestName name = new TestName();
    
    @BeforeClass
    public static void setup() throws IOException {
        
    }
    
    @Before
    public  void init() throws IOException, GeneralSecurityException {
        if (repositoryId == null) {
            List<ESRepository> repositories = repositoryApiClient.listRepositories();
            for (ESRepository repository : repositories) {
                if (repository.getType().equals(getDriveType())) {
                    repositoryId = repository.getId();
                    break;
                }
            }
        }
        
        if (repositoryId == null) {
            throw new NoSuchBeanDefinitionException(getDriveType().toString());
        }
        ESFile folder = fileApiClient.createFile(repositoryId, null, name.getMethodName(), name.getMethodName() + "_"+ OffsetDateTime.now().toString());
        folderId = folder.getId();
    }
    
    
    protected TypeEnum getDriveType() {
        return TypeEnum.FS;
    }

    @After
    public void clean() throws IOException {
        fileApiClient.deleteFile(folderId);
    }
    
    
    
    @Test
    public void testChildren() throws IOException {
        createFile();
        createFolder();
        ESFileList files = fileApiClient.getFileChildren(folderId, false, false, 100);
        // Assert
        assertEquals(2, files.getItems().size());
    }
    
    @Test
    public void testdownloadTo() throws IOException {
        String fileId = createFile();
        byte[] bytes = fileApiClient.getFileContent(fileId);
        
        assertArrayEquals(bytes, FILE.getBytes());
    }
    
    
    @Test
    public void testGetPermissions() throws IOException {
        List<ESPermission> permissions = fileApiClient.getFilePermissions(folderId);
        
        assertTrue(permissions.size()>=0);
    }
    
    
    @Test
    public void testDelete() throws IOException {
        String fileId = createFile();
        fileApiClient.deleteFile(fileId);
        boolean isfileNotfound = false;
        try {
            fileApiClient.getFile(fileId, false, false);
        } catch (FeignException e) {
            //I think the best way is to add exist method in file API
            isfileNotfound = true;
        }
        
        
        assertTrue(isfileNotfound);
    }
    
    @Test
    public void testGetFile() throws IOException {
        OffsetDateTime beforeCreate = OffsetDateTime.now();
        
        String fileId = createFile();
        ESFile file = fileApiClient.getFile(fileId, true, true);
        
        assertFalse(file.getDirectory());
        assertEquals(FILE.getName(), file.getName());
        assertEquals(FILE.getSize(), file.getFileSize().longValue());
        assertEquals(folderId, file.getParents().get(0).getId());
        
        assertEquals(DigestUtils.md5DigestAsHex(FILE.getBytes()), file.getChecksum());
        
        assertTrue(file.getCreationTime().isBefore(beforeCreate.plusSeconds(5)));
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
        
        assertNotNull(fileApiClient.getFile(fileId, false, false));
        
        ESFile file = fileApiClient.getFile(fileId, true, true);
        assertFalse(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    
    @Test
    public void testInsertFolder() throws IOException {
        String fileId = createFolder();
        
        ESFile file = fileApiClient.getFile(fileId, true, true);
        assertTrue(file.getDirectory());
        assertEquals(folderId, file.getParents().get(0).getId());
    }
    
    
    
    private String createFile() throws IOException {
        return  fileApiClient.createFile(folderId, FILE, FILE.getName(), "FDescription_" + CODE).getId();
    }
    
    private String createFolder() throws IOException {
        return fileApiClient.createFile(folderId, null, "DName_"+  CODE, "DDescription_" + CODE).getId();
    }
    
    
  
    
}
