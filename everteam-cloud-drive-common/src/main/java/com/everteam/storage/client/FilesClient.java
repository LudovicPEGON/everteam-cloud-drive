package com.everteam.storage.client;

import java.io.File;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.everteam.storage.common.model.ESFile;
import com.everteam.storage.common.model.ESFileList;
import com.everteam.storage.common.model.ESPermission;



@FeignClient("${everteam.feignclient.storage:storage-v1-0}")
public interface FilesClient {

    @RequestMapping(value = "/files/{id}/copy", method = RequestMethod.POST)
    ESFile copyFile(@PathVariable("id") String id,
           @RequestBody ESFile file);

    @RequestMapping(value = "/files/{id}", produces = { "application/json" }, consumes = {
            "multipart/form-data" }, method = RequestMethod.POST)
    ESFile createFile(@PathVariable("id") String id,
            @RequestParam("content") File content,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "description", required = false) String description);

    @RequestMapping(value = "/files/{id}", method = RequestMethod.DELETE)
    void deleteFile(@PathVariable("id") String id);

    @RequestMapping(value = "/files/{id}", produces = { "application/json" }, method = RequestMethod.GET)
    ESFile getFile(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions);

    @RequestMapping(value = "/files/{id}/children", produces = { "application/json" }, method = RequestMethod.GET)
    ESFileList getFileChildren(@PathVariable("id") String id,
            @RequestParam(value = "getPermissions", required = false, defaultValue = "false") Boolean getPermissions,
            @RequestParam(value = "maxResult", required = false, defaultValue = "100") Integer maxResult);

    @RequestMapping(value = "/files/{id}/content", produces = {
            "application/octet-stream" }, method = RequestMethod.GET)
    byte[] getFileContent(@PathVariable("id") String id);

    @RequestMapping(value = "/files/{id}/permissions", method = RequestMethod.GET)
    List<ESPermission> getFilePermissions(@PathVariable("id") String id);

    @RequestMapping(value = "/files/{id}/move", produces = { "application/json" }, consumes = {
            "multipart/form-data" }, method = RequestMethod.POST)
    ESFile moveFile(@PathVariable("id") String id,
            @RequestPart(value = "parentId", required = false) String parentId);

    @RequestMapping(value = "/files/{id}", produces = { "application/json" }, consumes = {
            "multipart/form-data" }, method = RequestMethod.PUT)
    ESFile updateFile(@PathVariable("id") String id,
            @RequestParam("file") File content,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description);

}
