package com.cheche.kong.connector;

import com.cheche.kong.KongConfig;
import com.cheche.util.FastJsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;


@Component
public class Connector {

    @Autowired
    private KongConfig config;

    private Retrofit retrofit;

    @PostConstruct
    private void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(config.getKongAdminUrl())
                .addConverterFactory(FastJsonFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
