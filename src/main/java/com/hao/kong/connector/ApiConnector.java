package com.hao.kong.connector;

import com.hao.model.api.Api;
import com.hao.model.api.Apis;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * API HTTP请求定义接口
 * Created by user on 2016/3/30.
 */
public interface ApiConnector {

    /**
     * 新增API
     * @param newApi
     * @return
     */
    @POST("/apis")
    Call<Api> add(@Body Api newApi);

    /**
     * api管理
     * @param queryInfo
     * @return
     */
    @GET("/apis")
    Call<Apis> query(@QueryMap Map<String,Object> queryInfo);

    /**
     * 查询API
     * @param apiId
     * @return
     */
    @GET("/apis/{apiId}")
    Call<Api> queryOne(@Path("apiId") String apiId);

    /**
     * Api信息修改  只有修改 无法创建
     * @param apiId
     * @param api
     * @return
     */
    @PATCH("/apis/{apiId}")
    Call<Api> update(@Path("apiId") String apiId,@Body Api api);

    /**
     *
     * @param api
     * @return
     */
    @PUT("/apis")
    Call<Api> updateOrCreate(@Body Api api);

    /**
     * 删除某个API 返回值为空
     * @param apiId
     * @return
     */
    @DELETE("/apis/{apiId}")
    Call<Api> delete(@Path("apiId") String apiId);
}
