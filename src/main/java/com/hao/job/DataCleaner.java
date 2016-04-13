package com.hao.job;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * 定时清除任务
 * Created by user on 2016/4/13.
 */
@Component
public class DataCleaner implements InitializingBean{

    private ScheduledExecutorService cleanExecutor = Executors.newSingleThreadScheduledExecutor();

    public void start() {
        cleanExecutor.scheduleAtFixedRate(() ->{
            clean();
        },1,24, TimeUnit.HOURS);
    }

    private void clean() {


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
