package com.everteam.storage.drive;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.everteam.storage.common.model.ESRepository.TypeEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneDriveTest extends DriveTest {

    @Override
    protected TypeEnum getDriveType() {
        return TypeEnum.ONEDRIVE;
    }

}
