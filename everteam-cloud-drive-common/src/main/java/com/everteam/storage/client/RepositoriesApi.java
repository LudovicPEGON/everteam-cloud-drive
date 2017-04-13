package com.everteam.storage.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.everteam.storage.common.model.ESRepository;

@FeignClient(name="${everteam.feignclient.storage:storage-v1-0}")
public interface RepositoriesApi {

    @RequestMapping(value = "/repositories", produces = { "application/json" }, method = RequestMethod.GET)
    List<ESRepository> listRepositories();

}
