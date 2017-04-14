package com.everteam.storage.drive;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.everteam.storage.common.model.ESRepository.TypeEnum;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GoogleDriveTest extends DriveTest {

    @Override
    protected TypeEnum getDriveType() {
        return TypeEnum.GOOGLE;
    }
    
    

}
