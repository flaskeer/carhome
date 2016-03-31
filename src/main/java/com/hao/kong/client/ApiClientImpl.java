package com.hao.kong.client;

import com.hao.kong.connector.ApiConnector;
import com.hao.kong.connector.Connector;
import com.hao.model.api.Api;
import com.hao.model.api.Apis;
import com.hao.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by user on 2016/3/30.
 */
@Component
public class ApiClientImpl implements ApiClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientImpl.class);

    @Autowired
    private Connector connector;

    private ApiConnector apiConnector;

    @PostConstruct
    private void init() {
        apiConnector = connector.create(ApiConnector.class);
    }

    @Override
    public Api add(Api api) throws IOException {
        Call<Api> call = apiConnector.add(api);
        Response<Api> response = call.execute();
        if (response.isSuccess()) {
            return response.body();
        }
        return Api.builder().errorMessage(response.errorBody().string()).build();
    }

    @Override
    public Apis query(Api api) throws IOException {
        Call<Apis> call = apiConnector.query(api == null ? null : api.toMap());
        Response<Apis> response = call.execute();
        if (response.isSuccess()) {
            return response.body();
        }
        return null;
    }

    @Override
    public Api queryOne(String apiId) throws IOException {
        Call<Api> call = apiConnector.queryOne(apiId);
        Response<Api> response = call.execute();
        if (response.isSuccess()) {
            return response.body();
        }
        return Api.builder().errorMessage(response.errorBody().string()).build();
    }

    @Override
    public Api update(Api api) throws IOException {
        Call<Api> call = apiConnector.update(api.getId(), api);
        Response<Api> response = call.execute();
        if (response.isSuccess()) {
            return response.body();
        }
        return Api.builder().errorMessage(response.errorBody().string()).build();
    }

    @Override
    public void delete(String apiId) throws IOException {
        Asserts.checkNotBlank(apiId,"待删除的API ID");
        Call<Api> call = apiConnector.delete(apiId);
        call.execute();
    }
}
