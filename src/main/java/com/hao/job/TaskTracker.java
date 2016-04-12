package com.hao.job;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2016/4/12.
 */
public class TaskTracker {

    private int poolSize;

    private Class<?> clazz;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(poolSize);


    public TaskTracker(int poolSize) {
        this.poolSize = poolSize;
    }

    public TaskTracker(int poolSize, Class<?> clazz) {
        this.poolSize = poolSize;
        this.clazz = clazz;
    }



    public void setJobRunnerClass(Class clazz) {
        this.clazz = clazz;
    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {

        },60,60, TimeUnit.SECONDS);
    }

}
