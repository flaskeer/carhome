package com.cheche.kong.client;

import com.cheche.model.api.Api;
import com.cheche.model.api.Apis;

import java.io.IOException;

/**
 * 调用kong API接口
 * Created by user on 2016/3/30.
 */
public interface ApiClient {

    /**
     * 增加一个
     * @param api
     * @return
     * @throws IOException
     */
    Api add(Api api) throws IOException;

    Apis query(Api api) throws IOException;

    Api queryOne(String apiId) throws IOException;

    Api update(Api api) throws IOException;

    void delete(String apiId) throws IOException;
}
