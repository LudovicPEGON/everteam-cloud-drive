package com.everteam.storage;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.everteam.storage.client.RepositoriesApi;
import com.everteam.storage.common.model.ESRepository;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest()
//@AutoConfigureMockMvc
public class ApplicationTest {

    //@Autowired
    //private MockMvc mockMvc;
    @Value("${storage-v1-0.ribbon.listOfServers}")
    String  test;
    
    @Autowired
    private RepositoriesApi api;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        List<ESRepository> respositories = api.listRepositories();
        
        
        
        /*this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("swagger-ui.html"));*/
    }
    
    
}