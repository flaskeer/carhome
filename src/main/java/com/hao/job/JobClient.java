package com.hao.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  任务客户端 用来提交任务
 * Created by user on
 * 2016/4/12.
 */
public class JobClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobClient.class);

    public Response submitJob(final List<Job> jobs) {
        final Response response = new Response();
        response.setSuccess(true);
        return response;
    }

}
