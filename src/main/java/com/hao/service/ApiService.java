package com.hao.service;

import com.hao.kong.response.RestfulResponse;
import com.hao.model.api.Api;

/**
 * Created by user on 2016/3/31.
 */
public interface ApiService {

    /**
     * 通过API ID查询API信息
     * @param apiId
     * @return
     */
    Api query(String apiId);

    /**
     * 保存API
     * @param api
     * @return 存储结果
     */
    RestfulResponse<String> save(Api api);

}
