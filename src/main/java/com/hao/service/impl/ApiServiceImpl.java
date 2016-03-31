package com.hao.service.impl;

import com.hao.kong.client.ApiClient;
import com.hao.kong.response.RestfulResponse;
import com.hao.model.api.Api;
import com.hao.service.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016/3/31.
 */
@Service
public class ApiServiceImpl implements ApiService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Autowired
    private ApiClient client;



    @Override
    public Api query(String apiId) {
        try {
            Api api = client.queryOne(apiId);
            LOGGER.info("API 信息：{}",api);
            return api;
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }

    @Override
    public RestfulResponse<String> save(Api api) {
        Api newApi;
        try {
            if (StringUtils.isBlank(api.getId())) {
                newApi = client.add(api);
            } else {
                newApi = client.update(api);
            }
        } catch (Exception e) {
            LOGGER.error("",e);
            return new RestfulResponse<>(e.getMessage());
        }
        if (StringUtils.isBlank(newApi.getErrorMessage())) {
            return new RestfulResponse<>();
        }
        return new RestfulResponse<>(newApi.getErrorMessage());
     }
}
