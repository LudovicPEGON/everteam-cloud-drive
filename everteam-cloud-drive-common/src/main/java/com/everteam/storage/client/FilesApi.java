package com.everteam.storage.client;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.everteam.feign.FeignSpringFormEncoder;
import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;

import feign.codec.Encoder;


@FeignClient(name="${everteam.feignclient.storage:storage-v1-0}", configuration=FilesApi.MultipartSupportConfig.class)
public interface FilesApi {
    @Configuration
    public class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder encoder() {
            return new FeignSpringFormEncoder();
        }
    }
    
    


    @RequestMapping(value = "/files/{id}/checkUpdates",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    Void checkUpdates(@PathVariable("id") String id,
        @RequestParam(value = "fromDate", required = true) OffsetDateTime fromDate);


    @RequestMapping(value = "/files/{id}/copy",
        method = RequestMethod.POST)
    ESFile copyFile(@PathVariable("id") String id,
        @RequestParam(value = "targetId", required = true) String targetId);

    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ESFile createFile(@PathVariable("id") String id,
            @PathVariable("file") MultipartFile content,
            @PathVariable(value = "name", required = false) String name,
            @PathVariable(value = "description", required = false) String description) ;


    @RequestMapping(value = "/files/{id}",
        method = RequestMethod.DELETE)
    Void deleteFile(@PathVariable("id") String id) ;


    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ESFile getFile(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue="false") Boolean getPermissions,
            @RequestParam(value = "getChecksum", required = false, defaultValue="false")  Boolean getChecksum);

    @RequestMapping(value = "/files/{id}/children",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ESFileList getFileChildren(@PathVariable("id") String id,
        @RequestParam(value = "getPermissions", required = false, defaultValue="false")  Boolean getPermissions,
        @RequestParam(value = "getChecksum", required = false, defaultValue="false") Boolean getChecksum,
        @RequestParam(value = "maxResult", required = false, defaultValue="100") Integer maxResult);

    @RequestMapping(value = "/files/{id}/content",
        produces = { "application/octet-stream" }, 
        method = RequestMethod.GET)
    byte[] getFileContent(@PathVariable("id") String id);


    @RequestMapping(value = "/files/{id}/permissions",
        method = RequestMethod.GET)
    List<ESPermission> getFilePermissions(@PathVariable("id") String id);

    @RequestMapping(value = "/files/{id}/move",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ESFile moveFile(@PathVariable("id") String id,
        @RequestParam(value = "targetId", required = true) String targetId);


    @RequestMapping(value = "/files/{id}",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.PUT)
    ESFile updateFile(@PathVariable("id") String id,
            @PathVariable("file") MultipartFile content,
            @PathVariable(value = "description", required = false) String description);
  
}
