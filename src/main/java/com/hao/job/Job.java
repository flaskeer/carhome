package com.hao.job;

import com.alibaba.fastjson.JSON;
import com.hao.exception.JobSubmitException;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/4/12.
 */
public class Job {

    @NotNull
    private String taskId;

    /**
     * 重试次数
     */
    private int retryTime = 0;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public void checkField() {
        if (taskId == null) {
            throw new JobSubmitException("taskId can not be null~ job is:" + toString());
        }
    }
}
